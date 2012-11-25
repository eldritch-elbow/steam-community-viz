library(psych)


# Assign data from to achievements object
achs <- read.table("achievements.txt", header=T)

# Plot histogram for data
hist(achs$AchCount)

# Get some descriptive stats
describe(achs)




# Assign data from achievement rates file
achrates <- read.table("ach-rates-full.txt", header=T, quote="|")

# Plot histogram for data
hist(achrates$Rate)
hist(achrates$Rate, breaks = 100)

# Get some descriptive stats
describe(achrates)
