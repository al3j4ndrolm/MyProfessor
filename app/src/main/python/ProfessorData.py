import ProfessorRatings

@DataClass
class ProfessorData:
	name: str
	# department: str
	schedule : list
	num_ratings: int
	difficulty: float
	overall_rating: float
	would_take_again: float