import HttpUtil
import time
import requests
import re

######################## Exported to be used in kotlin ########################

def get_ratings(professor_name):
    print(f"RatingsFetcher: Received call get_ratings({professor_name}).")
    start = time.time()

    professor = get_professor_by_school_and_name(professor_name)

    end = time.time()
    latency = round(end-start, 2)
    print(f"RatingsFetcher: Returning ratings data of get_ratings({professor_name}) to client in {latency} seconds.")
    return professor

def get_professor_by_school_and_name(professor_name: str):

    soup = HttpUtil.get_soup(url = f"https://www.ratemyprofessors.com/search/professors/1967?q={professor_name}")
    teacher_cards = soup.find_all('a', class_=re.compile(r"^TeacherCard_"))

    for teacher_card in teacher_cards:
        rating_row = teacher_card.find_all('div', class_=re.compile(r"^CardNumRating_"))
        if len(rating_row) > 3:
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
                        would_take_again = int(would_take_again_row[1].text.strip("%"))
                        continue
                if "level of difficulty" in info.text:
                    difficulty_row = info.find_all('div')
                    if len(difficulty_row) > 0:
                        difficulty = float(difficulty_row[0].text)
                        continue
            professor_dict = {
                "professor_name": professor_name,
                "difficulty": difficulty,
                "would_take_again": would_take_again,
                "overall_rating": rating_row[2].text,
                "review_num": review_num,
            }
            return professor_dict

    return {}

def build_professor_dick():
    rating_row = teacher_card.find_all('div', class_=re.compile(r"^CardNumRating_"))
    review_num = int(rating_row[3].text.strip("ratings"))

    if review_num == 0:
        return {
            "professor_name": professor_name,
            "difficulty": 0,
            "would_take_again": 0,
            "overall_rating": 0,
            "review_num": 0,
        }

    other_info = teacher_card.find_all('div', class_=re.compile(r"^CardFeedback_"))
    would_take_again = 0
    difficulty = 0.0

    for info in other_info:
        if "would take again" in info.text:
            would_take_again_row = info.find_all('div')
            if len(would_take_again_row) > 1:
                would_take_again = would_take_again_row[1].text.strip("%")
                print(f"would_take_again = [{would_take_again}]")
                would_take_again = int(would_take_again)
                continue
        if "level of difficulty" in info.text:
            difficulty_row = info.find_all('div')
            if len(difficulty_row) > 0:
                difficulty = float(difficulty_row[0].text)
                continue

    return {
        "professor_name": professor_name,
        "difficulty": difficulty,
        "would_take_again": would_take_again,
        "overall_rating": rating_row[2].text,
        "review_num": review_num,
    }

######################## Exported to be used in kotlin ########################

if __name__ == "__main__":
    print(get_ratings("John Jimenez"))
