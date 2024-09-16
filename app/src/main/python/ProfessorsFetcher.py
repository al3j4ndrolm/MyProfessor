import requests
from bs4 import BeautifulSoup
from dataclasses import asdict

def get_professors_data(department_code, coruse_code, term_code):
	url_to_fetch_courses = f'https://www.deanza.edu/schedule/listings.html?dept={department_code}&t={term_code}'
	page = requests.get(url_to_fetch_courses)
	soup = BeautifulSoup(page.text, 'html.parser')
	table = soup.find_all('table', 'table table-schedule table-hover mix-container')[0]
	rows = table.find_all('tr')

	professor_table = read_row_and_update_intructor_table(rows, department_code + " " + coruse_code)

	return list(professor_table.values())


def read_row_and_update_intructor_table(rows, full_coruse_code):
	professor_table = {}

	for row in rows:
		columns = row.find_all('td')

		# Check if the row has enough columns and matches the full_coruse_code
		if len(columns) > 7 and columns[1].text == full_coruse_code:
			professor_name = columns[7].text
			schedule = f"{get_days(columns[5].text)} - {columns[6].text}/{columns[8].text}"

			if professor_name in professor_table:
				professor_table[professor_name]["schedule"].append(schedule)  # Append schedule
			else:
				professor_table[professor_name] = {
					"name": convert_name(professor_name),
					"schedule": [schedule],
					"num_ratings": 0,
					"difficulty": 0.0,
					"overall_rating": 0.0,
					"would_take_again": 0.0,
				}

	return professor_table

def convert_name(name):
	parts = name.split(',')
	if len(parts) != 2:
		return name  # Return the original if it doesn't match the expected format

	last_name = parts[0].strip()
	first_name = parts[1].strip()
	formatted_name = f"{first_name.title()} {last_name.title()}"
	return formatted_name

def get_days(input):
	result = ""

	for day in input:
		if day != "·":
			result += day
	return input.strip("·")

if __name__ == "__main__":
	print(get_professors_data(department_code = "MATH", coruse_code = "1A", term_code = "F2024"))