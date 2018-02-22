import populartimes
from pprint import pprint
import logging

logging.getLogger().setLevel(logging.INFO)

key = "AIzaSyDOT4MIrfzdH7rhHt2J5obO2YC2q41UQaA"  # Add your API key
# print(populartimes.get_id(key, "ChIJo-2ZufR1nkcRWEZYKjOWbio"))

# deutsches museum
# (48.128442,11.581105), (48.130969, 11.584710)

# old
# (48.142199, 11.566126), (48.132986, 11.580047)

# innenstadt
# (48.132751, 11.565834), (48.142417,11.584288)

# viktualienmarkt
# (48.134521, 11.574845), (48.135451, 11.577446)


# (48.120179, 11.564494)
# (48.129345, 11.589728)

if __name__ == "__main__":
    # TESTING CURRENT POPULARITY
    # smallz jazz club NY: ChIJu55raJRZwokReRL9b1tLmQo
    # deutsches museum: ChIJ01CaE2PfnUcRUpb8Ylg-UXo
    # der pschorr: ChIJccSCl4p1nkcRzQDgOnxwKdA

    # mac-d im tal ChIJfXFTWYp1nkcRE_JLWvGyhM4
    # hofbräuhaus ChIJxfxSz4t1nkcRLxq9ze1wwak
    # kilians ChIJo-2ZufR1nkcRWEZYKjOWbio


    results = populartimes.get(key, ['bar'], (48.129345, 11.589728), (48.120179, 11.564494), radius=200)
    print(len(results))


    results = populartimes.get(key, ['cafe'], (37.4221145, -122.0860002), (37.4228775, -122.085133), radius=50)
    print(len(results))

    # sacre ceur ChIJg8vfy1xu5kcRA1tGDNGsgHA
    # louvre ChIJD3uTd9hx5kcR1IQvGfr8dbk
    # eiffel ChIJrbS_8-Fv5kcRzSjPvnUT03s
    # ChIJrbS_8-Fv5kcRzSjPvnUT03s
    print("START")

    bound_lower = (40.702195412680979, -73.94647996722307)
    bound_upper = (40.705036825361958, -73.94273259403137)
    result = populartimes.get(key, ["cafe"], bound_lower, bound_upper, n_threads=5)

    # TODO current popularity for pop.get area does not work
    print(len(result))
    print(result)


    # print("sacre-ceur")
    print(populartimes.get_id(key, "ChIJg8vfy1xu5kcRA1tGDNGsgHA"))

    # print("louvre")
    print(populartimes.get_id(key, "ChIJD3uTd9hx5kcR1IQvGfr8dbk"))

    # print("eiffel")
    print(populartimes.get_id(key, "ChIJrbS_8-Fv5kcRzSjPvnUT03s"))

    # TESTING FOR VERY LARGE AREA
    # pprint(populartimes.get(key, ["bar"], (48.132751, 11.565834), (48.142417,11.584288), radius=180, n_threads=1))

    #
    #print(populartimes.get(key, ["bar"], (48.132751, 11.565834), (48.142417, 11.584288), radius=180, n_threads=20))

    # print(populartimes.get(key, ["restaurant"], (48.134521, 11.574845), (48.135451, 11.577446),
    #                       radius=180, n_threads=10,
    #                       all_places=False))
