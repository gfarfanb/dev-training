from datetime import datetime, timedelta, date
from pywintypes import com_error
from sys import exit
from shutil import copyfile

import xlwings as xw
import configparser as conf

import os
import re
import argparse
import collections


DEFAULT_SETTINGS = "settings.ini"
USER_SETTINGS = "vacations.ini"

def load_workbook():
    parser = argparse.ArgumentParser()
    parser.add_argument("-f", "--file", help="Path of the vacacions registry file")
    parser.add_argument("-of", "--override_file", nargs=2, action="append", help="Overrides a value of the especified file configuration entry")
    parser.add_argument("-oc", "--override_calculation", nargs=2, action="append", help="Overrides a value of the especified file configuration entry")
    parser.add_argument("-s", "--select", action="store_true", help="Allows to select an employee from file. If the flag is not set, the calculation will be done for all employees")
    parser.add_argument("-w", "--write", action="store_true", help="Writes the balance in the file")
    args = parser.parse_args()

    config = conf.RawConfigParser()
    bundle_dir = os.path.abspath(os.path.dirname(__file__))
    working_dir = os.getcwd()

    user_settings_path = os.path.join(working_dir, USER_SETTINGS)

    print(user_settings_path)

    if not os.path.exists(user_settings_path):
        copyfile(os.path.join(bundle_dir, DEFAULT_SETTINGS), user_settings_path)

    config.read(user_settings_path)

    override_config("File", args.override_file, config)
    override_config("Calculation", args.override_calculation, config)

    file_path = args.file if args.file != None else config["File"]["FileToAnalizeLocation"]

    try:
        workbook = xw.Book(file_path)
    except FileNotFoundError:
        print("File not found: {}".format(file_path))
        exit()
    except KeyError:
        print("Configuration file not found: {}".format(user_settings_path))
        exit()

    return workbook, config, args


def override_config(group, values, config):
    if values == None:
        return

    for field_value in values:
        print("[{}:{}]".format(group, field_value[0]), "-",
              "'{}'".format(config[group][field_value[0]]),
              "replaced by",
              "'{}'".format(field_value[1])
              )
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


def select_employee(employees):
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
    days_column = config["File"]["AllowedDaysValueColumn"] if config["File"]["AllowedDaysValueColumn"] != None else ""
    days_row = config["File"]["AllowedDaysValueRow"] if config["File"]["AllowedDaysValueRow"] != None else ""

    if days_column != "" and days_row != "":
        raise ValueError("""
                         Define only one property:
                         'AllowedDaysValueColumn' for values on a column
                         'AllowedDaysValueRow' for values on a row
                         """)

    if days_column != "":
        while (employee_sheet.range(year_column + str(year_index)).value != None):
            year = int(employee_sheet.range(year_column + str(year_index)).value)
            days = int(employee_sheet.range(days_column + str(year_index)).value)

            allowed_per_year[str(year)] =  days
            year_index += 1

    if days_row != "":
        for column in employee_sheet.range(year_column + str(year_index)).expand('right'):
            column_part = get_column(column.get_address(False, False))
            year = int(column.value)
            days = int(employee_sheet.range(column_part + str(days_row)).value)

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


def get_balance_and_ratio(selected_employee, workbook, config):
    employee_sheet = workbook.sheets[selected_employee]

    start_date_value = employee_sheet[config["File"]["StartDateCell"]].value
    start_date_format = config["File"]["StartDateFormat"]

    if type(start_date_value) is datetime:
        start_date = start_date_value.date()
    elif type(start_date_value) is date:
        start_date = start_date_value
    elif type(start_date_value) is str:
        start_date = datetime.strptime(start_date_value, start_date_format).date()
    else:
        raise ValueError("Unexpected type for 'StartDateCell': {typr}".format(type=type(start_date_value)))

    today = datetime.now()

    allowed_per_year = get_allowed_per_year(selected_employee, workbook, config)
    taken_vacations_per_year = get_taken_vacations(selected_employee, workbook, config)

    total_days_year = int(config["Calculation"]["TotalDaysYear"])
    total_days_half_year = int(config["Calculation"]["TotalDaysHalfYear"])

    # enjoyed_days_value = employee_sheet[config["File"]["EnjoyedVacationsDaysCell"]].value
    # enjoyed_days = int(enjoyed_days_value) if enjoyed_days_value != None else 0
    previous_days_value = employee_sheet[config["File"]["PreviousVacationDaysCell"]].value
    previous_days = int(previous_days_value) if previous_days_value != None else 0

    # previous_days = enjoyed_days + previous_days

    pending_days = 0
    exceeded_days = previous_days

    taken_vacations = collections.OrderedDict(sorted(taken_vacations_per_year.items()))

    print()
    print("[{}]".format(selected_employee),
          "started-on=", start_date,
          "not-registered-vacation-days=", previous_days
          )
    print("Calculating balance...")

    for calculated_year, taken_days in taken_vacations.items():
        anniversary_date = datetime(int(calculated_year), start_date.month, start_date.day)
        taken_days_balance = taken_days + exceeded_days
        exceeded_days = 0

        if today < anniversary_date:
            print("allowed-on-year[{}]=".format(calculated_year), allowed_days,
                  "taken=", taken_days,
                  "taken-balance=", taken_days_balance,
                  "anniversary[UNFULFILLED]=", anniversary_date.date()
                  )
            exceeded_days += taken_days_balance
            continue

        allowed_days = allowed_per_year.get(calculated_year, 0)
        leftovers_days = 0

        if taken_days_balance > allowed_days:
            exceeded_days = taken_days_balance - allowed_days
        else:
            leftovers_days = allowed_days - taken_days_balance

        expiration_date = anniversary_date + timedelta(days=(total_days_year + total_days_half_year))

        if today < expiration_date:
            left_days_anniversary = (expiration_date - today).days

            if left_days_anniversary >= leftovers_days:
                pending_days += leftovers_days
            else:
                pending_days += leftovers_days - left_days_anniversary

        print("allowed-on-year[{}]=".format(calculated_year), allowed_days,
              "taken=", taken_days,
              "taken-balance=", taken_days_balance,
              "exceeded=", exceeded_days,
              "pending=", pending_days,
              "anniversary=", anniversary_date.date(),
              "expiration=", expiration_date.date()
              )

    return (pending_days - exceeded_days), get_ratio(start_date, allowed_per_year)


def get_ratio(start_date, allowed_per_year):
    print("Calculating ratio...")
    today = datetime.now()
    anniversary_date = datetime(today.year, start_date.month, start_date.day)

    if today < anniversary_date:
        anniversary_date = datetime(today.year - 1, start_date.month, start_date.day)

    worked_days = (today - anniversary_date).days
    allowed_days = allowed_per_year.get(str(anniversary_date.year), 0)

    if allowed_days == 0:
        print("Not registered year for anniversary:", anniversary_date.date())
        return 0

    print("allowed-on-year[{}]=".format(anniversary_date.year), allowed_days,
          "worked-days=", worked_days,
          "anniversary[LAST]=", anniversary_date.date()
          )

    return int((allowed_days / 365) * worked_days)


def process_balance(selected_employee, balance, ratio, workbook, config, args):
    print("[{name}] Balance: {balance} Ratio: {ratio}".format(
        name=selected_employee,
        balance=balance,
        ratio=ratio
        ))

    if not args.write:
        return

    try:
        balance_cell = config["File"]["BalanceDaysCell"]
        ratio_cell = config["File"]["RatioDaysCell"]

        employee_sheet = workbook.sheets[selected_employee]

        employee_sheet.range(balance_cell).value = balance
        employee_sheet.range(ratio_cell).value = ratio

        print("[{name}] Sheet update successful - Balance[{balance}] Ratio[{ratio}]".format(
            name=selected_employee,
            balance=balance_cell,
            ratio=ratio_cell)
            )
    except com_error as err:
        print("[{name}] Unable to write on sheet - {details}".format(
            name=selected_employee,
            details=err.args))


def main():
    workbook, config, args = load_workbook()

    try:
        employees = get_employees(workbook, config)

        if args.select:
            selected_employee = select_employee(employees)
            balance, ratio = get_balance_and_ratio(selected_employee, workbook, config)

            process_balance(selected_employee, balance, ratio, workbook, config, args)
        else:
            for employee in employees:
                balance, ratio = get_balance_and_ratio(employee, workbook, config)

                process_balance(employee, balance, ratio, workbook, config, args)
    except ValueError as err:
        print("Unable to process workbook - {details}".format(
            details=err.args))

    workbook.app.quit()


if __name__ == '__main__':

    exit(main())
