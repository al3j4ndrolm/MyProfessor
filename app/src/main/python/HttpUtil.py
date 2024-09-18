import requests
from bs4 import BeautifulSoup

def get_soup(url):
	page = requests.get(url)
	print(f"HttpUtil: Received web content from url[{url}].")
	return BeautifulSoup(page.text, 'html.parser')