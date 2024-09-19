# import HttpUtil
import time

######################## Exported to be used in kotlin ########################

def get_ratings(professor_name):
	professor_dict = {
		"professor_name": professor_name,
		"difficulty": 0.0,
		"would_take_again": 0.0,
		"overall_rating": 0.0,
		"review_num": 1,
	}

	print(f"RatingsFetcher: Received call get_ratings({professor_name}).")
	
	time.sleep(2)

	print(f"RatingsFetcher: Returning ratings data of get_ratings({professor_name}) to client.")

	return professor_dict

######################## Exported to be used in kotlin ########################

if __name__ == "__main__":
	print(get_ratings("any"))
