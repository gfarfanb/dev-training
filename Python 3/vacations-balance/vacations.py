from datetime import datetime, timedelta
from pywintypes import com_error
from sys import argv, exit
from os import path

import xlwings as xw
import configparser as conf

import re
import argparse


SETTINGS = "settings.ini"

def load_workbook():
    parser = argparse.ArgumentParser()
    parser.add_argument("-f", "--file", help="Path of the vacacions registry file")
    parser.add_argument("-of", "--override_file", nargs=2, action="append", help="Overrides a value of the especified file configuration entry")
    parser.add_argument("-oc", "--override_calculation", nargs=2, action="append", help="Overrides a value of the especified file configuration entry")
    parser.add_argument("-s", "--select", action="store_true", help="Allows to select an employee from file. If the flag is not set, the calculation will be done for all employees")
    parser.add_argument("-w", "--write", action="store_true", help="Writes the balance in the file")
    parser.add_argument("-r", "--ratio", action="store_true", help="Enables calculation based on ratio")
    args = parser.parse_args()

    config = conf.RawConfigParser()
    bundle_dir = path.abspath(path.dirname(__file__))
    config.read( path.join(bundle_dir, SETTINGS))

    override_config("File", args.override_file, config)
    override_config("Calculation", args.override_calculation, config)

    file_path = args.file if args.file != None else config["File"]["FileToAnalizeLocation"]

    try:
        workbook = xw.Book(file_path)
    except FileNotFoundError:
        print("File not found: {}".format(file_path))
        exit()
    except KeyError:
        print("Configuration file not found: {}".format(SETTINGS))
        exit()

    return workbook, config, args


def override_config(group, values, config):
    if values == None:
        return

    for field_value in values:
        print("[{}:{}]".format(group, field_value[0]), "-",
              "'{}'".format(config[group][field_value[0]]),
              "replaced by",
              "'{}'".format(field_value[1]))
        config[group][field_value[0]] = field_value[1]


def get_column(range):
    return re.sub(r'\d+$', '', range)


def get_index(range):
    return int(re.sub(r'^[a-zA-Z]+', '', range))


def get_employees(workbook, config):
    base_year = int(config["Calculation"]["BaseYear"])
    employees = set()

    for sheet_name in workbook.sheet_names:
        if sheet_name.isdigit() and int(sheet_name) >= base_year:
            sheet = workbook.sheets[sheet_name]
            employee_column = get_column(config["File"]["EmployeeNameFirstCell"])
            employee_index = get_index(config["File"]["EmployeeNameFirstCell"])

            while (sheet.range(employee_column + str(employee_index)).value != None):
                employee = sheet.range(employee_column + str(employee_index)).value

                try:
                    employee_sheet = workbook.sheets[employee]
                    employees.add(employee_sheet.name)
                except KeyError:
                    print("Sheet for employee not found: {name}".format(name=employee))

                employee_index += 1

    return employees


def select_employee(employees, workbook):
    if len(employees) == 0:
        print("There are no employees in the file")
        exit()

    print("Select a employee:")
    index = 1
    for employee in sorted(employees):
        print("{i}) {name}".format(i=index, name=employee))
        index += 1

    print("employee-index> ")
    selected_employee_index = -1
    try:
        selected_employee_index = int(input()) - 1
    except ValueError:
        print("Invalid employee index")
        exit()

    try:
        selected_employee = sorted(employees)[selected_employee_index]
    except IndexError:
        print("Invalid employee index")
        exit()

    return selected_employee


def get_allowed_per_year(selected_employee, workbook, config):
    employee_sheet = workbook.sheets[selected_employee]
    allowed_per_year = {}

    year_column = get_column(config["File"]["AllowedDaysYearFirstCell"])
    year_index = get_index(config["File"]["AllowedDaysYearFirstCell"])
    days_column = config["File"]["AllowedDaysValueColumn"]

    while (employee_sheet.range(year_column + str(year_index)).value != None):
        year = int(employee_sheet.range(year_column + str(year_index)).value)
        days = int(employee_sheet.range(days_column + str(year_index)).value)

        allowed_per_year[str(year)] =  days
        year_index += 1

    return allowed_per_year


def get_taken_vacations(selected_employee, workbook, config):
    base_year = int(config["Calculation"]["BaseYear"])
    vacations_per_year = {}

    for sheet_name in workbook.sheet_names:
        if sheet_name.isdigit() and int(sheet_name) >= base_year:
            sheet = workbook.sheets[sheet_name]
            employee_column = get_column(config["File"]["EmployeeNameFirstCell"])
            employee_index = get_index(config["File"]["EmployeeNameFirstCell"])

            while (sheet.range(employee_column + str(employee_index)).value != None):
                value = sheet.range(employee_column + str(employee_index)).value

                if value == selected_employee:
                    total_column = config["File"]["TotalTakenDaysColumn"]
                    vacations_per_year[sheet_name] = int(sheet.range(total_column + str(employee_index)).value)
                    break

                employee_index += 1

    return vacations_per_year


def get_balance(selected_employee, workbook, config, args):
    employee_sheet = workbook.sheets[selected_employee]

    start_date_value = employee_sheet[config["File"]["StartDateCell"]].value
    start_date_format = config["File"]["StartDateFormat"]
    start_date = datetime.strptime(start_date_value, start_date_format).date()

    print()
    print(selected_employee, "started on", start_date)

    allowed_per_year = get_allowed_per_year(selected_employee, workbook, config)
    taken_vacations_per_year = get_taken_vacations(selected_employee, workbook, config)

    today = datetime.now()

    total_days_year = int(config["Calculation"]["TotalDaysYear"])
    total_days_half_year = int(config["Calculation"]["TotalDaysHalfYear"])

    pending_days = 0
    exceeded_days = 0

    for calculated_year, days_allowed in allowed_per_year.items():
        anniversary_date = datetime(int(calculated_year), start_date.month, start_date.day)
        expiration_date = anniversary_date + timedelta(days=(total_days_year + total_days_half_year))

        if today < anniversary_date:
            print("Unfulfilled anniversary:", anniversary_date)
            continue

        worked_days = (today - anniversary_date).days

        if args.ratio and worked_days < total_days_year:
            allowed_days = int((days_allowed / 365) * worked_days)
        else:
            allowed_days = days_allowed

        taken_days = taken_vacations_per_year.get(calculated_year, 0) + exceeded_days
        exceeded_days = 0
        leftovers_days = 0

        if taken_days > allowed_days:
            exceeded_days = taken_days - allowed_days
        else:
            leftovers_days = allowed_days - taken_days

        if today < expiration_date:
            left_days_anniversary = (expiration_date - today).days

            if left_days_anniversary >= leftovers_days:
                pending_days += leftovers_days
            else:
                pending_days += leftovers_days - left_days_anniversary

        print("Year:", calculated_year,
            "Allowed:", allowed_days,
            "Taken:", taken_days,
            "Expiration date:", expiration_date.date(),
            "Exceeded days:", exceeded_days,
            "Pending days:", pending_days)

    return (pending_days - exceeded_days)


def process_balance(selected_employee, balance, workbook, config, args):
    print("Balance for {name}: {result}".format(name=selected_employee, result=balance))

    if not args.write:
        return

    try:
        balance_cell = config["File"]["BalanceDaysCell"]
        employee_sheet = workbook.sheets[selected_employee]
        employee_sheet.range(balance_cell).value = balance

        print("Updated sheet for {name}".format(name=selected_employee))
    except com_error as err:
        print("Unable to write update sheet for {name} - {details}".format(name=selected_employee, details=err.args))


def main():
    workbook, config, args = load_workbook()

    employees = get_employees(workbook, config)

    if args.select:
        selected_employee = select_employee(employees, workbook)
        balance = get_balance(selected_employee, workbook, config, args)

        process_balance(selected_employee, balance, workbook, config, args)
    else:
        for employee in employees:
            balance = get_balance(employee, workbook, config, args)

            process_balance(employee, balance, workbook, config, args)

    workbook.app.quit()


if __name__ == '__main__':

    exit(main())
