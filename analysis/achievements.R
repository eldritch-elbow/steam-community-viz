library(psych)

# Assign data from to counts object
achs <- read.table("achievements.txt", header=T)

# Print basic data
class(achs)
names(achs)

# Plot histogram for data
hist(achs$Count)

# Get some descriptive stats
describe(achs)
