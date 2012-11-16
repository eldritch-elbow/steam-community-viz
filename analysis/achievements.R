library(psych)

# Assign data from to achievements object
achs <- read.table("achievements.txt", header=T)

# Plot histogram for data
hist(achs$AchCount)

# Get some descriptive stats
describe(achs)



# Assign data from to counts object
achrates <- read.table("achievement-rates.txt", header=T, quote="\")

# Plot histogram for data
hist(achrates$Rate)

# Get some descriptive stats
#describe(achrates)
