"""
Rishitha Ravi
Intermediate Data Programming
"""
from cse163_utils import normalize_token


class Document:
    '''
    This is a class representing an individual document 
    in the corpus. Stores the term frequency information 
    for each word in the document, as well as document path.
    '''

    def __init__(self, relative_path):
        '''
        Initializes a document object. 
        Given the relative_path, stores it as an instance field,
        and also calculates the term frequency of each word
        within that file
        '''
        self._relative_path = relative_path
        # caches tf info
        self._tf = self._calc_tf()

    def _calc_tf(self):
        '''
        Normalizes each word in the file found through 
        the instance field _relative_path, and creates a
        dictionary for the term frequency of each word
        '''
        word_count = 0
        term_freq = dict()
        with open(self._relative_path) as f:
            lines = f.readlines()
            for line in lines:
                words = line.split()
                for word in words:

                    # deals with punctuation, case
                    word = normalize_token(word)
                    if word != '':
                        word_count += 1
                        # avoids key error
                        if word not in term_freq:
                            term_freq[word] = 1
                        else:
                            term_freq[word] = term_freq[word] + 1

        # divides each term_count by number of words in document
        # term frequency
        for key, value in term_freq.items():
            term_freq[key] = value / word_count
        return term_freq

    def get_words(self):
        '''
        Returns a list of all words within the file, 
        w/o duplicates
        '''

        # .keys() returns a view object
        return list(self._tf.keys())

    def term_frequency(self, term):
        '''
        Given a term, returns the term frequency for that term.
        If that term is not in the tf dictionary, returns 0
        '''
        norm_term = normalize_token(term)

        # avoids key error
        if norm_term not in self._tf:
            return 0
        else:
            return self._tf[norm_term]

    def get_path(self):
        '''
        Returns the relative path of a document
        '''
        # needs this line before for syntax formatting
        return self._relative_path
