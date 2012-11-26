library(psych)

# ACHIEVEMENT COUNTS FOR GAMES
# Assign data from to achievements object
achs <- read.table("achievements.txt", header=T)

# Plot histogram for data
hist(achs$AchCount)

# Get some descriptive stats
describe(achs)



# GLOBAL ACHIEVEMENT RATES
# Assign data from achievement rates file
achrates <- read.table("ach-rates-full.txt", header=T, quote="|")

# Plot histogram for data
hist(achrates$Rate)
hist(achrates$Rate, breaks = 100)

# Get some descriptive stats
describe(achrates)



# ACHIEVEMENT RATES FOR USER

achrates <- read.table("user-ach-rates.txt", header=T, quote="~")
ar.hum <- subset(achrates, User == "Humiliator")
ar.jw <- subset(achrates, User == "jediwannabe")
ar.dav <- subset(achrates, User == "David")

hist(ar.hum$Rate)
hist(ar.hum)
