import locale
import sys
import re
locale.setlocale(locale.LC_ALL, 'en_US')
from sys import argv


# I defined a function to find sentences
def split_into_sentences(text):
    text = text.split("..")  # I did that because it was difficult to separate the 3 dots.
    text = " ".join(text)
    sentence_endings = r"[.?!]"  # I defined the end of sentence characters.
    sentences = re.split(sentence_endings,text)  # This command separates the sentences but leaves a space at the end, so I subtracted 1 when printing the number at the end.
    return sentences

    #This function lists words in decreasing order of frequency.
def sort_word(words_and_frequencies):
        return sorted(sorted(words_and_frequencies),key=lambda x:x[1],reverse=True)
def main():

    # I defined lists which i used
    words=[]
    characters_just_word=[]
    characters=[]
    words_and_frequencies=[]
    support_list=[]
    the_shortest_words=[]
    the_longest_words=[]

    f=open(argv[1],"r")

    text=f.read()
    sentences=split_into_sentences(text) #I found sentences.
    f.close()

    f=open(argv[1],"r")
    for i in f.read():
        characters.append(i)  # this command separates all characters
    f.close()
    f=open(argv[1],"r")

    for line in f:
        for word in line.split(): #I separated the words according to the spaces
            words.append(word.strip(".,?!:;')(").lower()) #If there were unwanted characters at the beginning and end of the word, I removed them with this command and converted the words to lowercase letters.
    for i in words:
        # I just separated the characters in the words
        for j in re.findall(".",i):
            characters_just_word.append(j)


    for i in words:
        if i not in support_list:
            support_list.append(i) #I used the support list to write the following code more easily.
            words_and_frequencies.append([i,words.count(i)/len(words)]) #This command creates a two-item list containing the word and its frequency and adds it to the words_and_frequencies.


    x="" #I define a string of length 0 to avoid errors.
    for i in words:
         if len(i)> len(x):
            x=i  #I found the longest word by comparison
    for i in words:
        if len(x)==len(i):
            if [i,words.count(i)/len(words)] not in the_longest_words:#If the longest word is more than one, I find them and their freguencies with this command
                the_longest_words.append([i,words.count(i)/len(words)]) #I found the longest words and their frequencies


    y=x
    # x is the longest word.Then, I set it equal to another variable.I compared from the longest to find the smallest.
    for i in words:
        if len(i)<len(y):
            y=i
    for i in words:
        if len(i)==len(y):
            #If there is more than one shortest word and there is more than one of them, I used this command so that it does not have to be written again.
            if [i,words.count(i)/len(words)] not in the_shortest_words:
                the_shortest_words.append([i,words.count(i)/len(words)]) #I found the shortest words and their frequencies


    p=open(argv[2],"w")
    # I printed the outputs according to their format and order.
    p.write("{:17}{:7}:\n".format("Statistics about",argv[1]))
    p.write("{:24}:{}{:}\n".format("#Words"," ",len(words)))
    p.write("{:24}:{}{:}\n".format("#Sentences "," ",len(sentences)-1))
    p.write("{:24}:{}{:.2f}\n".format("#Words/#Sentences"," ",len(words)/(len(sentences)-1)))
    p.write("{:24}:{}{:}\n".format("#Characters"," ",len(characters)))
    p.write("{:24}:{}{:}\n".format("#Characters (Just Words)"," ",len(characters_just_word)))
    if len(the_shortest_words)==1:
        #If there is a smallest word and I listed it as a binary, I printed the 1st element of the list as the word and the 2nd element as the frequency.
        for group in the_shortest_words:
            p.write("{:24}:{}{:25}({:.4f})\n".format("The Shortest Word"," ",group[0],group[1]))
    else:
        #If there is more than one smallest word and I list it as binary, I sort it correctly with the sort_word function that I defined. I print the 1st element of the list as a word and the 2nd element as its frequency.
        p.write("{:24}:\n".format("The Shortest Words "))
        for group in sort_word(the_shortest_words):
            p.write("{:25}({:.4f})\n".format( group[0], group[1]))


    if len(the_longest_words)==1:
        for group in the_longest_words:
            #If there is a longest word and I listed it as a binary, I printed the 1st element of the list as the word and the 2nd element as the frequency.
            p.write("{:24}:{}{:25}({:.4f})\n".format("The Longest Word"," ",group[0],group[1]))
    else:
        #If there is more than one longest word and I list it as binary, I sort it correctly with the sort_word function that I defined. I print the 1st element of the list as a word and the 2nd element as its frequency.
        p.write("{:24}:\n".format("The Longest Words"))
        for group in sort_word(the_longest_words):
            p.write("{:25}({:.4f})\n".format(  group[0], group[1]))

    p.write("{:24}:\n".format("Words and Frequencies "))
    for group in sort_word(words_and_frequencies) :
        #I sorted all the words with the sort_word function that I defined, and since I listed them in binary form, I printed the 1st element of the list as the word and the 2nd element as its frequency.
        p.write("{:24}:{}{:.4f}\n".format(group[0]," ",group[1]))
    p.close()


if __name__ == "__main__":
    main()
