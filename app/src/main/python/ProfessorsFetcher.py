import HttpUtil

######################## Exported to be used in kotlin ########################

def get_terms():
	print(f"ProfessorsFetcher: Received call get_terms().")
	soup = HttpUtil.get_soup(url = 'https://www.deanza.edu/schedule/')
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

	print(f"ProfessorsFetcher: Returning term data of get_terms() to client.")
	return terms

def get_professors_data(department_code, coruse_code, term_code):
	print(f"ProfessorsFetcher: Received call get_professors_data({department_code}, {coruse_code}, {term_code}).")
	try:
		soup = HttpUtil.get_soup(url = f'https://www.deanza.edu/schedule/listings.html?dept={department_code}&t={term_code}')
		result = soup.find_all('table', 'table table-schedule table-hover mix-container')
		if len(result) == 0:
			return ["shilabens"]

		table = result[0]
		rows = table.find_all('tr')
		professor_table = build_professor_table(rows, department_code + " " + coruse_code)
		print(f"ProfessorsFetcher: Returning professor data of get_professors_data(...) to client.")
		return list(professor_table.values())
	except:
		return ["shilabens"]

######################## Exported to be used in kotlin ########################

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
	schedules.append(format_schedule(schedule))

	following_row_i = start_row_i + 1
	while following_row_i < len(rows):
		columns_for_following_row = rows[following_row_i].find_all('td')
		if len(columns_for_following_row) < 7:
			schedule = build_schedule(columns = columns_for_following_row, days_col = 1, hours_col = 2, location_col = 4)
			if schedule:
				schedules.append(schedule)
			following_row_i += 1
		else:
			break

	return schedules

def format_schedule(schedule):
	if schedule:
		return schedule
	else:
		return "No schedule/ONLINE"

def build_schedule(columns, days_col, hours_col, location_col):
	days_in_week = get_days(columns[days_col].text)
	hours = columns[hours_col].text
	if "TBA" in hours:
		return None
	else:
		location = columns[location_col].text
		schedule = f"{days_in_week} - {hours}/{location}"
		return schedule

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
	print(get_professors_data(department_code = "aa", coruse_code = "", term_code = "F2024"))
	# print(get_professors_data(department_code = "PHYS", coruse_code = "2A", term_code = "F2024"))
	# print(get_terms())