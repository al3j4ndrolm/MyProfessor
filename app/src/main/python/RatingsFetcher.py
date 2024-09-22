import HttpUtil
import re
from DepartmentCode import departments

######################## Exported to be used in kotlin ########################

## Returns one of these:
## - a full dict with ratings -> professor is found with ratings
## - an empty dict -> professor is not found or no ratings yet
## - a dict with only "error" -> something went wrong

def get_ratings(professor_name: str, department_code: str):
	print(f"RatingsFetcher: Received call get_ratings({professor_name}, {department_code}).")
	
	professor = get_professor_ratings(professor_name, department_code)

	try:
		professor = get_professor_ratings(professor_name, department_code)

		print(f"RatingsFetcher: Returning ratings data of get_ratings({professor_name}) to client.")
		return professor
	except:
		return {"error": "Shilabens"}

######################## Exported to be used in kotlin ########################

def get_professor_ratings(professor_name: str, department_code: str):
	soup = HttpUtil.get_soup(url = f"https://www.ratemyprofessors.com/search/professors/1967?q={professor_name}")
	teacher_cards = soup.find_all('a', class_=re.compile(r"^TeacherCard_"))

	for teacher_card in teacher_cards:
		department_from_card = teacher_card.find_all('div', class_=re.compile(r"^CardSchool__Department"))[0].text
		departments_to_match = departments.get(department_code, [])
		if department_from_card in departments_to_match or departments_to_match == []:
			rating_dict = build_rating_dict(teacher_card)
			return rating_dict

	return {}

def build_rating_dict(teacher_card):
	rating_row = teacher_card.find_all('div', class_=re.compile(r"^CardNumRating_"))
	review_num = int(rating_row[3].text.strip("ratings"))

	if review_num == 0:
		return {}

	other_info = teacher_card.find_all('div', class_=re.compile(r"^CardFeedback_"))
	would_take_again = 0
	difficulty = 0.0

	for info in other_info:
		if "would take again" in info.text:
			would_take_again_row = info.find_all('div')
			if len(would_take_again_row) > 1:
				would_take_again = would_take_again_row[1].text.strip("%")
				try:
					would_take_again = int(would_take_again)
				except:
					would_take_again = -1
				continue
		if "level of difficulty" in info.text:
			difficulty_row = info.find_all('div')
			if len(difficulty_row) > 0:
				difficulty = float(difficulty_row[0].text)
				continue

	return {
		"difficulty": difficulty,
		"would_take_again": would_take_again,
		"overall_rating": rating_row[2].text,
		"review_num": review_num,
	}

######################## Exported to be used in kotlin ########################

if __name__ == "__main__":
	print(get_ratings("Sarah Lisha", "EWRT"))
