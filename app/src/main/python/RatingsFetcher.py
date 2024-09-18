# import HttpUtil
import time

######################## Exported to be used in kotlin ########################

def get_ratings(professor_name):
	print(f"RatingsFetcher: Received call get_ratings({professor_name}).")
	
	time.sleep(2)

	print(f"RatingsFetcher: Returning ratings data of get_ratings({professor_name}) to client.")
	return []

######################## Exported to be used in kotlin ########################

if __name__ == "__main__":
	print(get_ratings("any"))