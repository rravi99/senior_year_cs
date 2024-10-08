'''
Rishitha Ravi
'''
from cse163_utils import assert_equals
from cse163_utils import normalize_paths
from document import Document
from search_engine import SearchEngine
import math


# Testing Document Class Methods
def test_get_words():
    '''
    Tests the get_words method within the Document class
    '''
    # Tests normal case
    # sorts because order doesn't matter
    norm = Document('student_tests/normal.txt')
    assert_equals(sorted(["this", "is", "a", "super", "normal", "file", 
                          "that", "should", "be", "pretty", "easy", 
                          "to", "understand"]), sorted(norm.get_words()))

    # Tests empty file
    empty = Document('student_tests/empty.txt')
    assert_equals([], empty.get_words())

    # Tests file with many instances of same word
    dupe = Document('student_tests/dupes.txt')
    assert_equals(["pie"], dupe.get_words())

    # Tests file with many instances of same word, 
    # but w/ different case and punctation. 
    # Mixed number/letter case should count as a different word
    dupe_norm = Document('student_tests/dupes_norm.txt')
    assert_equals(["pie", "pi"], dupe_norm.get_words())

    # Tests file with numbers
    # file has a 'word' that is combination of numbers and letters
    # sorts because order doesn't matter
    numbers = Document('student_tests/number.txt')
    assert_equals(sorted(["1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "pie"]), 
                  sorted(numbers.get_words()))

    # Tests file with only punctuation
    punct = Document("student_tests/punctuation.txt")
    assert_equals([], punct.get_words())


def test_term_frequency():
    '''
    Tests the term_frequency method within the Document class
    '''
    # Tests normal case
    norm = Document('student_tests/normal.txt')
    assert_equals(2 / 14, norm.term_frequency('pretty'))
    assert_equals(1 / 14, norm.term_frequency('this'))

    # Tests for not-normalized term
    assert_equals(2 / 14, norm.term_frequency('PRET!TY!!'))

    # Tests when word not in file
    assert_equals(0, norm.term_frequency('pie')) 

    # Tests with empty file
    empty = Document('student_tests/empty.txt')
    assert_equals(0, empty.term_frequency('word'))

    # Tests with numbers
    numbers = Document('student_tests/number.txt')
    assert_equals(1 / 11, numbers.term_frequency("1983"))

    # Number/letter combination: 1984pie, so there should be
    # two instances of pie: pie, and 1984pie
    assert_equals(2 / 11, numbers.term_frequency("pie"))

    # Tests with a term that is just punctuation
    punct = Document('student_tests/punctuation.txt')
    assert_equals(0, punct.term_frequency("?!"))


def test_get_path():
    '''
    Tests the term_frequency method within the Document class
    '''
    # Tests w/o nested subdirectory
    norm = Document('student_tests/normal.txt')
    assert_equals('student_tests/normal.txt', norm.get_path())

    # Tests w/ nested subdirectory
    nested = Document('student_tests/sub/normal.txt')
    assert_equals('student_tests/sub/normal.txt', nested.get_path())


def test_calc_tf():
    '''
    Tests the private calc_tf method within the Document class
    '''
    # Tests normal case
    norm = Document('student_tests/normal.txt')
    assert_equals({"this": 1 / 14, "is": 1 / 14, "a": 1 / 14, "super": 1 / 14, "normal": 1 / 14, 
                   "file": 1 / 14, "that": 1 / 14, "should": 1 / 14, "be": 1 / 14, 
                   "pretty": 2 / 14, "easy": 1 / 14, "to": 1 / 14, "understand": 1 / 14}, norm._calc_tf())

    # Tests empty file
    empty = Document('student_tests/empty.txt')
    assert_equals({}, empty._calc_tf())

    # Tests file with many instances of same word
    dupe = Document('student_tests/dupes.txt')
    assert_equals({"pie": 1}, dupe._calc_tf())

    # Tests file with many instances of same word
    # but w/ different case and punctation.
    # Mixed number/letter case should count as a different word
    dupe_norm = Document('student_tests/dupes_norm.txt')
    assert_equals({"pie": 16 / 17, "pi": 1 / 17}, dupe_norm._calc_tf())

    # Tests file with numbers
    # file has a 'word' that is combination of numbers and letters: 1984pie
    numbers = Document('student_tests/number.txt')
    assert_equals({"1975": 1 / 11, "1976": 1 / 11, "1977": 1 / 11, 
                   "1978": 1 / 11, "1979": 1 / 11, "1980": 1 / 11, 
                   "1981": 1 / 11, "1982": 1 / 11, "1983": 1 / 11, 
                   "pie": 2 / 11}, numbers._calc_tf())

    # Tests file with only punctuation
    punct = Document("student_tests/punctuation.txt")
    assert_equals({}, punct._calc_tf())


# Testing Search Engine Class Methods
def test_get_docs():
    '''
    Tests the private _get_docs method within the SearchEngine class. 
    Uses get_path instead of creating new Document objects 
    because of reference semantics
    '''
    # order doesn't matter here, so sort lists to pass tests
    # Tests the 'normal case', given a directory w/ no sub-directories
    norm = SearchEngine('student_tests/sub')

    assert_equals(['student_tests/sub/normal.txt'], 
                  [doc.get_path() for doc in norm._get_docs('student_tests/sub')])

    # Tests the case with sub-directories
    sub = SearchEngine('student_tests')
    assert_equals(sorted(['student_tests/sub/normal.txt', 'student_tests/dupes_norm.txt', 
                          'student_tests/dupes.txt', 'student_tests/empty.txt',
                          'student_tests/normal.txt', 'student_tests/number.txt',
                          'student_tests/punctuation.txt']),
                  sorted([doc.get_path() for doc in sub._get_docs('student_tests')]))

    # Tests case where path to a file, not directory is passed
    file = SearchEngine('student_tests/sub/normal.txt')
    assert_equals(['student_tests/sub/normal.txt'],
                  [doc.get_path() for doc in file._get_docs('student_tests/sub/normal.txt')])

    # Tests case where directory doesn't exist
    inval = SearchEngine('fake_dir')
    assert_equals([], inval._get_docs('fake_dir'))


def test_calculate_idf():
    '''
    Tests the private _calculate_idf method within the SearchEngine class, 
    by comparing with its result stored in the instance field _idf
    '''
    # math.log(x) returns ln(x)
    # Tests the files within student_tests
    # Tests empty file
    # Tests not-normalized words in file
    norm = SearchEngine('student_tests')
    norm_expected = {"this": math.log(7 / 2), "is": math.log(7 / 2), 
                     "a": math.log(7 / 2), "normal": math.log(7 / 2), 
                     "file": math.log(7 / 2), "that": math.log(7 / 2), "should": math.log(7 / 2), 
                     "be": math.log(7 / 2), "pretty": math.log(7 / 2), "super": math.log(7 / 2), 
                     "easy": math.log(7 / 2), "to": math.log(7 / 2), "understand": math.log(7 / 2), 
                     "pie": math.log(7 / 3), "pi": math.log(7 / 1), "1975": math.log(7 / 1), 
                     "1976": math.log(7 / 1), "1977": math.log(7 / 1), "1978": math.log(7 / 1), 
                     "1979": math.log(7 / 1), "1980": math.log(7 / 1), "1981": math.log(7 / 1), 
                     "1982": math.log(7 / 1), "1983": math.log(7 / 1)}

    # sorts by key using dictionary comprehension to make sure both have same order
    # so that tests pass
    assert_equals({key: norm_expected[key] for key in sorted(list(norm_expected.keys()))},
                  {key: norm._idf[key] for key in sorted(list(norm._idf.keys()))})

    # Tests case where there's only a single file in directory
    single = SearchEngine('student_tests/sub')
    single_expected = {"this": math.log(1 / 1), "is": math.log(1 / 1),
                       "a": math.log(1 / 1), "super": math.log(1 / 1),
                       "normal": math.log(1 / 1), "file": math.log(1 / 1), "that": math.log(1 / 1), 
                       "should": math.log(1 / 1), "be": math.log(1 / 1), "pretty": math.log(1 / 1), 
                       "easy": math.log(1 / 1), "to": math.log(1 / 1), "understand": math.log(1 / 1)}

    assert_equals({key: single_expected[key] for key in sorted(list(single_expected.keys()))},
                  {key: single._idf[key] for key in sorted(list(single._idf.keys()))})

    # Tests case where a term appears in every document
    every = SearchEngine('every')
    every_expected = {"the": math.log(3 / 3), "dog": math.log(3 / 1), 
                      "frog": math.log(3 / 1), "bog": math.log(3 / 1)}

    assert_equals({key: every_expected[key] for key in sorted(list(every_expected.keys()))}, 
                  {key: every._idf[key] for key in sorted(list(every._idf.keys()))})

    # Tests just an empty file
    empty = SearchEngine("student_tests/empty.txt")
    assert_equals({}, empty._idf)


def test_tf_idf():
    '''
    Tests the private tf-idf method within the SearchEngine class
    which returns the tf-idf score for a query within a specific document
    '''
    eng = SearchEngine('student_tests')
    # Part One: Single word queries
    norm = Document('student_tests/normal.txt')
    assert_equals((2 / 14) * math.log(7 / 2), eng._tf_idf(norm, 'pretty'))

    # Test for non-normalized term
    assert_equals((2 / 14) * math.log(7 / 2), eng._tf_idf(norm, 'PreTt!Y?!!'))

    # Test for a term not in the document
    assert_equals(0, eng._tf_idf(norm, 'pizza'))

    # Test for term that appears once in a document
    assert_equals((1 / 14) * math.log(7 / 2), eng._tf_idf(norm, "this"))

    # Test for term that doesn't appears in document but appears in corpus
    assert_equals((0 / 14) * math.log(7 / 1), eng._tf_idf(norm, "1983"))

    # Test for punctuation 'term'
    punct = Document('student_tests/punctuation.txt')
    assert_equals(0, eng._tf_idf(punct, '?'))

    # Test for term that appears many times in a document
    dupes_norm = Document('student_tests/dupes_norm.txt')
    assert_equals((16 / 17) * math.log(7 / 3), eng._tf_idf(dupes_norm, 'pie'))

    # Number terms
    numbers = Document('student_tests/number.txt')

    assert_equals((1 / 11) * math.log(7 / 1), eng._tf_idf(numbers, "1975"))

    # Mixed word: 1984pie, should normalize to just pie
    assert_equals(0, eng._tf_idf(numbers, "1984"))

    # Test with empty document
    empty = Document('student_tests/empty.txt')
    assert_equals(0, eng._tf_idf(empty, "pie"))

    # Part Two: Multiple word queries
    # Tests two words, both in doc
    assert_equals(((2 / 14) * math.log(7 / 2)) + ((1 / 14) * math.log(7 / 2)),
                  eng._tf_idf(norm, 'this pretty'))

    # Tests two words, only one in doc
    assert_equals(((2 / 14) * math.log(7 / 2)) + 0,
                  eng._tf_idf(norm, 'frog pretty'))

    # Tests multiple words (> 2)
    assert_equals(((2 / 14) * math.log(7 / 2)) + ((1 / 14) * math.log(7 / 2)) + ((1 / 14) * math.log(7 / 2)),
                  eng._tf_idf(norm, 'this is pretty'))

    assert_equals(((2 / 14) * math.log(7 / 2)) + 
                  ((1 / 14) * math.log(7 / 2)) + 
                  ((1 / 14) * math.log(7 / 2)) + ((1 / 14) * math.log(7 / 2)),
                  eng._tf_idf(norm, 'this is super pretty'))

    # Tests extra spaces
    assert_equals(((2 / 14) * math.log(7 / 2)) + ((1 / 14) * math.log(7 / 2)),
                  eng._tf_idf(norm, 'this             pretty'))

    # Tests weird punctuation and casing
    assert_equals(((2 / 14) * math.log(7 / 2)) + ((1 / 14) * math.log(7 / 2)), 
                  eng._tf_idf(norm, 'th?!is     !?        prEtTy'))


def test_search():
    '''
    Tests the public search method in SearchEngine class
    '''
    # Test a query that appears in none of the documents
    flat_eng = SearchEngine('every')

    # Tests query not in any document
    assert_equals([], normalize_paths(flat_eng.search("jolly")))

    # Tests with corpus where multiple documents have tf_idf of 0
    # Tests query that appears in all documents in corpus
    assert_equals([], normalize_paths(flat_eng.search("the")))

    # Tests without subdirectories
    assert_equals(normalize_paths(["every/file1.txt"]), normalize_paths(flat_eng.search("dog")))

    # Tests with subdirectories
    basic_eng = SearchEngine('student_tests')

    # assumes sorted by tf-idf, then files without that word aren't listed
    assert_equals(normalize_paths(["student_tests/dupes.txt", "student_tests/dupes_norm.txt",
                                   "student_tests/number.txt"]),
                  normalize_paths(basic_eng.search("pie")))

    # Not normalized query (irregular punctuation and spacing)
    assert_equals(normalize_paths(["student_tests/dupes.txt", "student_tests/dupes_norm.txt",
                                   "student_tests/number.txt"]), 
                  normalize_paths(basic_eng.search("PiE!!???!?")))

    # Tests query that appears in all documents in corpus (varying tf)
    # Should still return no results (idf of 0)
    all_eng = SearchEngine("vary_tf")
    assert_equals([], normalize_paths(all_eng.search("this")))


def main():
    # calls document tests
    test_get_words()
    print('get_words passes')
    test_term_frequency()
    print('term_frequency passes')
    test_get_path()
    print('get_path passes')
    test_calc_tf()
    print('_calc_tf passes')
    print('all document tests pass')

    # calls search engine tests
    test_get_docs()
    print('_get_docs passes')
    test_calculate_idf()
    print('_calculate_idf passes')
    test_tf_idf()
    print('_tf_idf passes')
    test_search()
    print('search passes')

    print('all search engine tests pass')
    print('all tests pass') 


# To run this file in Repl.It, update the .replit file
if __name__ == '__main__':
    main()
