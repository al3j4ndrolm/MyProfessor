import requests
import re
import json
import base64
import os

from bs4 import BeautifulSoup
from flask import Flask, jsonify, request
from professorSchedule import ProfessorScheduleData
from dataclasses import asdict
import ratemyprofessor

app = Flask(__name__)
instructor_table = {}

def get_days(input):

	result = ""

	for day in input:
		if day != "·":
			result += day
	return result


def read_row_and_update_intructor_table(columns, instructors, COURSE_CODE):
    # Check if the row has enough columns and if it matches the desired course code
    if len(columns) > 7 and columns[1].text == COURSE_CODE:
        instructor = columns[7].text  # Extract instructor name

        # If the instructor is already in the dictionary, update their information
        if instructor in instructors:

        	instructors[instructor]["schedule"].append(f"{get_days(columns[5].text)} - {columns[6].text}/{columns[8].text}")  # Append schedule

        else:
            # Initialize a new entry for the instructor
            instructors[instructor] = {
                "name": instructor,
                "schedule": [f"{get_days(columns[5].text)} - {columns[6].text}/{columns[8].text}"]
            }



def convert_name(name):
    parts = name.split(',')
    if len(parts) != 2:
        return name  # Return the original if it doesn't match the expected format

    last_name = parts[0].strip()
    first_name = parts[1].strip()

    formatted_name = f"{first_name.title()} {last_name.title()}"
    return formatted_name

@app.route('/api/professorsSchedule', methods=['GET'])
def get_professors_data():

	COURSE = request.args.get('COURSE', '')
	COURSE_CODE = request.args.get('COURSE_CODE', '')
	TERM = request.args.get('TERM', '')
	URL = f'https://www.deanza.edu/schedule/listings.html?dept={COURSE}&t={TERM}'
	page = requests.get(URL)
	soup = BeautifulSoup(page.text, 'html.parser')
	table = soup.find_all('table', 'table table-schedule table-hover mix-container')[0]
	rows = table.find_all('tr')

	instructor_table = {}

	for row in rows:

		columns = row.find_all('td')
		read_row_and_update_intructor_table(columns, instructor_table, COURSE + " " + COURSE_CODE)

	professors = []

	for instructor, details in instructor_table.items():

		professor = ratemyprofessor.get_professor_by_school_and_name(convert_name(instructor))

		if professor is None:
			professors.append(ProfessorScheduleData(
				name = instructor, 
				schedule = details['schedule'],
				overall_rating = 0, 
				difficulty =  0, 
				would_take_again = 0,
				num_ratings = 0
				)
			)
		else:
			professors.append(ProfessorScheduleData(
				name = instructor,
				schedule = details['schedule'],
				overall_rating = professor.rating, 
				difficulty =  professor.difficulty, 
				would_take_again = professor.would_take_again,
				num_ratings = professor.num_ratings
				)
			)

	prof_schedules_dict_list = [asdict(prof) for prof in professors]

	return jsonify(prof_schedules_dict_list)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=4000, debug=True)

