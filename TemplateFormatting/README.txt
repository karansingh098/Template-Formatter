Author: Karan Singh
TemplateFormatFrame.java provides a GUI with the following features:

> Input Text Area: allows user to manually input text or browse for a file which the program
then extracts the text and prints it into the input text area. 
> Browse Button: allows user to search for a file. If user clicks browse button but does not
select a file, a NullPointerException is thrown
> Process Button: when clicked, interprets the text within the input text area and formats it 
outputs the formatted text into the output text area, retaining spacing
> Output Text Area: displays the formatted text
-------------------------------------------------------------------------------------------------
Standard Formatting Rules:
- Lines that start with ! are terminals with associated assignments
- When there is an @ symbol followed by a valid terminal, the words are substituted and formatted
	Ex.
	Input:
		!name1 = John
		!name2 = Smith
		Hello, @name1 @name2!
		
	Output: Hello, John Smith!
---------------------------------------------------------------------------------------------------
Behaviors and Special Cases:
- Throws IllegalArgumentException if there is a line starting with ! without an assignment
	Ex. '!name1' would be invalid input
		'!name1 = John' would be valid input
		
- User can put ! terminals in any order
	Ex.
	input:
	 1.	!name1=John Q.
		!name2=Smith
		Dear, @name1 @name2
		
	 2. Dear, @name1 @name2
	    !name2=Smith
		!name1=John Q.
		
	Both return same output: Dear, John Q. Smith
	
- When there is an @{} sequence, the substitution only occurs within the {} not the 
entire word following the @ symbol
	Ex. 
	input:
	!product=Horcrux Widget
	Thank you for your interest in @{product}s.
	
	output:
	Thank you for your interest in Horcrux Widgets.
	
- When there are multiple consecutive @ symbol's, program reduces it to one @ symbol
	Ex. 
	 input: 
	 1. @@@@hello
	 2. @ @ @hello
	Both return same output: @hello
	
- TemplateFormatter formats weirdly tabbed input but retains where line break are as well as spacing between the word
	Ex.
	input:  !name1=John Q.
	//weird tabs						!name2=Smith
							!salutation=Dear @name1 @name2!        
	output: Hello John Smith!
  