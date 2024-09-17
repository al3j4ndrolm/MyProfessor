import requests
from bs4 import BeautifulSoup
from dataclasses import asdict

def get_terms():
	soup = get_soup(url = 'https://www.deanza.edu/schedule/')
	sections = soup.find_all('fieldset')
	if len(sections) == 0:
		return []
	buttons = sections[0].find_all('button')

	terms = []
	for button in buttons:
		if "btn-term" in button.get('class'):
			terms.append({
				"term_code":button.get('value'),
				"term_text":button.text,
				})

	return terms

def get_professors_data(department_code, coruse_code, term_code):
	soup = get_soup(url = f'https://www.deanza.edu/schedule/listings.html?dept={department_code}&t={term_code}')
	table = soup.find_all('table', 'table table-schedule table-hover mix-container')[0]
	rows = table.find_all('tr')

	professor_table = build_professor_table(rows, department_code + " " + coruse_code)
	return list(professor_table.values())

def get_soup(url):
	page = requests.get(url)
	return BeautifulSoup(page.text, 'html.parser')

def build_professor_table(rows, full_coruse_code):
	professor_table = {}

	for i in range(len(rows)):
		row = rows[i]
		columns = row.find_all('td')

		# Check if the row has enough columns and matches the full_coruse_code
		if len(columns) > 7 and columns[1].text == full_coruse_code:
			professor_name = columns[7].text
			class_code = columns[0].text
			schedules = build_schedules(rows, i)

			if professor_name in professor_table:
				professor_table[professor_name]["all_schedules"][class_code] = schedules
			else:
				professor_table[professor_name] = {
					"name": convert_name(professor_name),
					"all_schedules": {class_code: schedules},
					"num_ratings": 0,
					"difficulty": 0.0,
					"overall_rating": 0.0,
					"would_take_again": 0.0,
				}

	return professor_table

def build_schedules(rows, start_row_i):
	schedules = []

	columns = rows[start_row_i].find_all('td')
	schedule = build_schedule(columns = columns, days_col = 5, hours_col = 6, location_col = 8)
	schedules.append(schedule)

	following_row_i = start_row_i + 1
	while following_row_i < len(rows):
		columns_for_following_row = rows[following_row_i].find_all('td')
		if len(columns_for_following_row) < 7:
			schedule = build_schedule(columns = columns_for_following_row, days_col = 1, hours_col = 2, location_col = 4)
			schedules.append(schedule)
			following_row_i += 1
		else:
			break

	return schedules

def build_schedule(columns, days_col, hours_col, location_col):
	schedule = f"{get_days(columns[days_col].text)} - {columns[hours_col].text}/{columns[location_col].text}"
	if not "TBA" in schedule:
		return schedule
	else:
		return "No schedule/ONLINE"

def convert_name(name):
	parts = name.split(',')
	if len(parts) != 2:
		return name  # Return the original if it doesn't match the expected format

	last_name = parts[0].strip()
	first_name = parts[1].strip()
	formatted_name = f"{first_name.title()} {last_name.title()}"
	return formatted_name

def get_days(input):
	return input.replace("·", "")

if __name__ == "__main__":
	print(get_professors_data(department_code = "MATH", coruse_code = "1A", term_code = "F2024"))
	# print(get_professors_data(department_code = "PHYS", coruse_code = "2A", term_code = "F2024"))
	# print(get_terms())