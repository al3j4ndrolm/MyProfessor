# import HttpUtil
import time
import random
import ratemyprofessor

######################## Exported to be used in kotlin ########################

def get_ratings(professor_name):
	print(f"RatingsFetcher: Received call get_ratings({professor_name}).")

	professor = ratemyprofessor.get_professor_by_school_and_name(professor_name)

	professor_dict = {
		"professor_name": professor_name,
		"difficulty": professor.difficulty,
		"would_take_again": professor.would_take_again,
		"overall_rating": professor.rating,
		"review_num": professor.num_ratings,
	}

	
	# time.sleep(random.randint(0, 1))
	# professor_dict = {
	# 	"professor_name": professor_name,
	# 	"difficulty": 2.3,
	# 	"would_take_again": 72.0,
	# 	"overall_rating": 3.0,
	# 	"review_num": 2,
	# }

	print(f"RatingsFetcher: Returning ratings data of get_ratings({professor_name}) to client.")
	return professor_dict

######################## Exported to be used in kotlin ########################

if __name__ == "__main__":
	print(get_ratings("Sarah Lisha"))
