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

	try:
		professor = get_professor_ratings(professor_name, department_code)

		print(f"RatingsFetcher: Returning ratings data of get_ratings({professor_name}) to client.")
		return professor
	except Exception as e:
		return {"error": e}

######################## Exported to be used in kotlin ########################

def get_professor_ratings(professor_name: str, department_code: str):
	search_name = professor_name.split("-")[0]
	print(f"professor_name: {professor_name}")

	soup = HttpUtil.get_soup(url = f"https://www.ratemyprofessors.com/search/professors/1967?q={search_name}")
	teacher_cards = soup.find_all('a', class_=re.compile(r"^TeacherCard_"))

	result = {}

	for teacher_card in teacher_cards:
		department_from_card = teacher_card.find_all('div', class_=re.compile(r"^CardSchool__Department"))[0].text
		departments_to_match = departments.get(department_code, "")

		if match(department_from_card, departments_to_match):
			name_from_card = teacher_card.find_all('div', class_=re.compile(r"^CardName__StyledCardName"))[0].text
			if similar_names(professor_name, name_from_card):
				rating_dict = build_rating_dict(teacher_card)
				if rating_dict.get("review_num", 0) > result.get("review_num", 0):
					result = rating_dict

	return result

def similar_names(name1, name2):
	first1, last1 = name1.split(" ", 1)
	first2, last2 = name2.split(" ", 1)

	match_in_order = match(first1, first2) and match(last1, last2)
	match_flip_order = match(first1, last2) and match(last1, first2)

	return match_in_order or match_flip_order

def match(str1, str2):
	return str1 in str2 or str2 in str1

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
	print(get_ratings("Misako Van Der Poel", "MATH"))
