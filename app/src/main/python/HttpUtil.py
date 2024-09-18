import requests
from bs4 import BeautifulSoup

def get_soup(url):
	page = requests.get(url)
	return BeautifulSoup(page.text, 'html.parser')