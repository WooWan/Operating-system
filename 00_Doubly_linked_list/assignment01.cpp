#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <typeinfo>

using namespace std;

//define Node struch which has degree, coefficient, Node* next, Node* prev
typedef struct Node {
	int degree;
	int coefficient;
	struct Node* next;
	struct Node* prev;
}Node;

void addNode(struct Node** head, int degree, int coefficient) {
	//allocate sizeof(struct Node) in heap area which is kind of memory area
	struct Node* new_node = (struct Node*)malloc(sizeof(struct Node));
	//ptr point current Node
	struct Node* ptr = *head;
	new_node->coefficient = coefficient;
	new_node->degree = degree;

	//if head is null, set new node as head, and it's prev and next is also null
	if (*head == NULL) {
		new_node->prev = NULL;
		new_node->next = NULL;
		*head = new_node;
		return;
	}


	//ptr end the search, if ptr->degree<=new_node->degree or ptr->next is null
	while (ptr->degree > new_node->degree ) {
		if (ptr->next == NULL) {
			break;
		}
		ptr = ptr->next;
	}
	// if ptr->degree and new_node->degree are same, combine coefficients.
	if (ptr->degree == new_node->degree) {
		ptr->coefficient = ptr->coefficient + new_node->coefficient;

		//if degree are different, connect node using doubly linked list.
	}else {
		//	case: When a new_node connects to a header(if ptr->prev is NULL)
		if ((ptr->prev == NULL) && (ptr->degree<new_node->degree)) {
			*head = new_node;
			new_node->prev = NULL;
			new_node->next = ptr;
			ptr->prev = new_node;
		}
		//	case: When a new_node connects to a tail(when ptr->next is NULL).
		else if (ptr->next == NULL) {
			//case: when ptr->degree> new_node->degree: insert new_node after ptr node
			if (ptr->degree > new_node->degree) {
				new_node->prev = ptr;
				new_node->next = NULL;
				ptr->next = new_node;
			}
			//case: when ptr->degree < new_node->degree: insert new_node before ptr node
			else {
				ptr->prev->next = new_node;
				new_node->next = ptr;
				ptr->prev = new_node;
				new_node->prev = ptr->prev;
			}
			
		}
		// case: When new node connects between other two nodes.
		else {
			ptr->prev->next = new_node;
			new_node->prev = ptr->prev;
			ptr->prev = new_node;
			new_node->next = ptr;
		}
	}
}
 void printNode(struct Node** head) {
	 //to print polynomial ascending order, set tail which point last node
	 struct Node* tail = *head;
	 while (tail->next != NULL) {
		 tail = tail->next;
	 }
	 struct Node* current = tail;
	 //print information of node, searching connected node.
     while (current != NULL) {
		 //if current degree is 1 
		 if (current->degree == 1)  printf(" %dx ", current->coefficient);
		 //if current degree is 0
		 else if (current->degree == 0) printf(" %d ", current->coefficient);
		 else {
			 printf(" %dx^%d ", current->coefficient, current->degree);
		 }
		 //if current->prev is not null, print '+'
		 if (current->prev!= NULL) {
			 printf("+");
		 }
		 //searcching linked node
		current = current->prev;
     }
	 printf("\n");
 }
 
 Node *multiply(struct Node** head1, struct Node** head2) {
	 //set mult_head which is multiplication of head1 and head2
	 struct Node* mult_head = NULL;
	 int mult_degree, mult_coefficient;

	 //multiply node of polynomial 'a', and node of polynomial 'b' 
	 //send mult_degree and mult_coefficient to add node.
	 for (Node* i = *head1; i != NULL; i = i->next) {
		 for (Node* j = *head2; j != NULL; j = j->next) {
			 mult_degree = (i->degree) + (j->degree);
			 mult_coefficient = i->coefficient * j->coefficient;
			 addNode(&mult_head, mult_degree, mult_coefficient);
		 }
	 }
	 // return header of mult_node
	 return mult_head;
 }

 Node* inputPoly() {
	 //set head(head is header of node)
	 struct Node* head = NULL;
	 int degree, coefficient;
	 while (1) {
		//take degree, coefficient
		 //scanf("%d %d", &degree, &coefficient);
		 //if user did not enter integer, take input again
		 if (scanf_s("%d", &degree) == 0 || scanf_s("%d", &coefficient) == 0) {
			 printf("Inputs must be an Integer\n");
			 printf("Please enter input again.");
			 rewind(stdin);
		 }

		//Case: both degree and coefficient are negative, disregard final input and return head node.
		 if (degree < 0 && coefficient < 0) {
			 printf("Done \n" );
			 return head;
		 }
		 //Case :degree<0 or coefficient <=0, take input again
		 else if ((degree < 0 || coefficient <= 0 )) {
			 printf("Enter input again\n");
			 continue;
		 }

		 //if degree and coefficient are postivie, call addNode function.
		 else {
			 addNode(&head, degree, coefficient);
		 }
	 }
 }
int main() {
	//head1 and head2 are header of polynomials.
	//take input for two header
	struct Node* head1 = inputPoly();
	struct Node* head2 = inputPoly();

	//print polynomial 'a' and polynomial 'b'
	printf("Poly 1 is:");
	printNode(&head1);
	printf("Poly 2 is:");
	printNode(&head2);

	//multiply the two polynomials ¡®a¡¯ and ¡®b¡¯ 
	//function return multiplication result. 
	printf("Multiplication of poly1 and poly2 is:");
	struct Node* multiply_result= multiply(&head1 , &head2);

	//print multiplication of 'a' and 'b'
	printNode(&multiply_result);
	
	//unallocation head1 memory in heap area 
	for (Node* i = head1; i != NULL; i = i->next) {
		free(i);
	}
	//unallocation head2 memory in heap area 
	for (Node* i = head2; i != NULL; i = i->next) {
		free(i);
	}

	return 0;


}