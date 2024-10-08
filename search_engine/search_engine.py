"""
Rishitha Ravi
Intermediate Data Programming
"""
import os 
import math
from cse163_utils import normalize_token
from document import Document


class SearchEngine:
    '''
    This class represents a Search Engine for a corpus of Documents. 
    Stores the Inverse Document Frequency of words
    '''
    def __init__(self, relative_path):
        self._relative_path = relative_path
        self._docs = self._get_docs(self._relative_path)
        self._idf = self._calculate_idf()

    def get_docs_length(self):
        '''
        Purely to handle FileNotFound; 
        if self._docs has a length of 0, then the
        directory that was passed in must not have existed
        Allows display of the proper output in main
        '''
        return len(self._docs)

    def _get_docs(self, rel_path):
        '''
        Gets and creates all the documents using the argument rel_path
        '''
        doc_list = []
        # takes an argument for rel_path instead of using instance field
        # in order to use the same method recursively
        # for nested directories
        try:
            # handles if path to one file, not directory, is passed in 
            # more robust user interaction
            if os.path.isfile(rel_path):
                doc_list.append(Document(rel_path))
            else:
                for name in os.listdir(rel_path):
                    # have to add the first part to make it a proper relative path
                    if os.path.isfile(rel_path + "/" + name):
                        doc_list.append(Document(rel_path + "/" + name))
                    elif os.path.isdir(rel_path + "/" + name):
                        # adds each item in returned list as an indiv item
                        # by recursively calling on the subdirectory.
                        # extend the list because append would add the entire list as item
                        doc_list.extend(self._get_docs(rel_path + "/" + name))
            return doc_list
        except FileNotFoundError as error:
            # handles FileNotFoundError
            print(error)
            return doc_list

    def _calculate_idf(self):
        '''
        Calculates the inverse document frequency for all words within the corpus
        '''
        idf = {}
        for doc in self._docs:
            # makes a key value pair showing number of docs each word appears in 
            # for denominator of idf
            for word in doc.get_words():
                if word not in idf:
                    idf[word] = 1
                else:
                    idf[word] = idf[word] + 1

        # calculates actual idf
        # divides # of documents in corpus by key (number of docs the term appears in)
        # takes the ln of the value
        doc_num = len(self._docs)

        for key, value in idf.items():
            # with only one parameter x, returns ln of x
            idf[key] = math.log(doc_num / value)

        return idf

    def _word_idf(self, term):
        '''
        Returns the idf for a specific term. 
        Returns 0 if the word isn't in the corpus
        '''

        # doesn't appear in any document in corpus
        if term not in self._idf:
            return 0
        else:
            return self._idf[term]

    def _tf_idf(self, doc, term):
        '''
        Returns the tf-idf for a query given a specific document
        '''
        total_tf_idf = 0

        # Handles multi-term query
        # .split() to handle extra spaces
        terms = term.split()
        for item in terms:
            item = normalize_token(item)
            total_tf_idf = total_tf_idf + doc.term_frequency(item) * self._word_idf(item) 

        return total_tf_idf

    def search(self, query):
        '''
        Given a query, or search term(s), 
        returns a list of document relative paths sorted by tf-idf
        '''
        # creates a dictionary for tf-idf
        # so the tf-idfs are linked to the relative paths of their documents
        # and can be used to sort the dict
        tf_idfs = {}

        # can't use the tf-idf values as keys 
        # because multiple documents might have the same tf-idf
        for document in self._docs:

            # if tf_idf is 0 for any of the words, doesn't add it to the list of results
            # either it's not in the document, or it's in every document (or none) in corpus
            if (self._tf_idf(document, query) != 0):
                tf_idfs[document.get_path()] = self._tf_idf(document, query)
            tf_idfs = dict(sorted(tf_idfs.items(), key=lambda x: x[0]))

        # sorts in descending order so most relevant is highest
        # so the most relevant is closer to beginning of list
        # uses a lambda expression
        # used w3w schools for help 
        tf_idfs = dict(sorted(tf_idfs.items(), key=lambda x: x[1], reverse=True))

        # returns the values, or document paths sorted in descending order based on tf-idf
        # casts to a list, since .keys() returns a view object
        return list(tf_idfs.keys())