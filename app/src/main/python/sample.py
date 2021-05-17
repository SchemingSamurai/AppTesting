from datetime import date

def main():
# Read the current date
    current_date = date.today()

# Print the formatted date
    return "Today is :%d-%d-%d" % (current_date.day,current_date.month,current_date.year)