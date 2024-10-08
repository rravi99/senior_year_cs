'''
Rishitha Ravi
'''
from search_engine import SearchEngine
from cse163_utils import normalize_paths


def main():
    '''
    Controls the user interaction with SearchEngine, initializing it to the proper directory
    and then handling inputted queries
    '''
    # changes end arg so user types answer on same line
    print("Please enter the name of a directory: ", end="")
    rel_path = input()
    print("Building Search Engine...")
    print()
    engine = SearchEngine(rel_path)

    # prompts the user to enter a directory name until they enter one that exists
    if engine.get_docs_length() == 0:
        print("Please enter the name of a VALID directory: ", end="")
        rel_path = input()
        print("Building Search Engine...")
        engine = SearchEngine(rel_path)
        while engine.get_docs_length() == 0:
            print("Please enter the name of a VALID directory: ", end="")
            rel_path = input()
            print("Building Search Engine...")
            engine = SearchEngine(rel_path)

    # once engine is initialized, handles queries
    print("Enter a search term to query (Enter=Quit): ", end="")
    query = input()
    new_line = True
    while (query != ""):
        print("Displaying results for '" + query + "':")
        for result in format_results(engine, query):
            # indents each result
            print("    " + result)
            new_line = (result != ("No results :("))
        # If no results are found, doesn't print a new line before the enter prompt
        # Helps pass the input/output test
        if (new_line):
            print()
        print("Enter a search term to query (Enter=Quit): ", end="")
        query = input()

    print("Thank you for searching.")


def format_results(engine, query):
    '''
    Takes the list of document paths returned after a query to and fits the main output format
    '''
    results_list = engine.search(query)

    # if term is not in corpus
    if (len(results_list) == 0):
        return ["No results :("]

    formatted_list = []
    index = 1

    # normalizes paths
    results_list = normalize_paths(results_list)

    # truncates result list if len > 10
    if len(results_list) > 10:
        results_list = results_list[0:10]

    for path in results_list:

        # formats path further to only display the filename on the other side of the dash
        # in line with expected output test
        path = path.replace(" - ", "/")
        broken_path = path.split("/")
        formatted_list.append(str(index) + ". " + 
                              ("/".join(broken_path[0: len(broken_path) - 1])) + 
                              " - " + broken_path[len(broken_path) - 1])
        # number for search result ranking
        index += 1

    return formatted_list


if __name__ == '__main__':
    main()