
from bs4 import BeautifulSoup

import requests
import csv

# Sandbox site
url = 'https://books.toscrape.com'
response = requests.get(url)

print(response.status_code)
print(response.text)

soup = BeautifulSoup(response.text, 'html.parser')
# Explore with an explorer HTML inspector to find the required elements
books = soup.find_all('article', class_='product_pod')

with open('output/books.csv', 'w', newline='') as f:
    writer = csv.writer(f)
    writer.writerow(['Title', 'Price'])

    for book in books:
        title = book.h3.a['title']
        price = book.find('p', class_='price_color').text

        print(f"{title} - {price}")
        writer.writerow([title, price])

# Consider:
#   - Playwright for pagination
#   - Scrapy for JavaScript rendering
