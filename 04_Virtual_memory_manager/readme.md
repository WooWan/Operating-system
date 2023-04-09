Course: Operating Systems Design(146043-31001), 2021
Name: Chang wan Woo
Programming Assignment NUMBER: Implementation assignment #4
Due DATE: 2021/06/01

Program environment
Window 10, Interllij, java version "1.8.0_291",
Java(TM) SE Runtime Environment (build 1.8.0_291-b10)

How to start?
1.Unzip file into eclipse workspace. 2. Import ( File > import > existing Projects into workspace

3. Browse > implementation_assignment4(it is located in your workspace)

By these steps you can show my program an run my program as well. There are three algorithms to start. If you enter 0, algorithm without page replacement is proceeded. If you enter 1, virtual memory with FIFO algorithm is proceeded and lastly if you enter 2, virtual memory with LRU algorithm is proceeded.
Finally, you can see the result for TLB hit rate and number of page fault on the command line.

Problem statement:
In memory management, we use logical memory to separate physical memory to enhance performance. So, in this project I mapped logical memory and physical memory with page replace algorithms: FIFO and LRU. Also, we can see number of page fault and TLB hit rate for each algorithm.

Software design:
To make LRU and FIFO algorithms, I used linked list and queue in this project. When program need to replace page, each algorithm selects victims and replace its page and allocate frame. In LRU algorithm, linked list operated stack. If page reference is requested, program first check TLB, and if page is in TLB, it refers TLB, otherwise it looks page table. If there is page, it refers corresponding physical memory and otherwise it allocates free frame for request.
I implemented LRU and FIFO algorithms when size is larger than 128 which is frame size for two algorithms. In this process each algorithm selects victim for replacement. When replacement is occurred, I print out which frame is reallocated to page.

Analyzation
We need to know that main difference between original implementation and modification version is size of physical memory. The former does not happen page replacement because page size is larger than number of page faults. However, in FIFO and LRU algorithms page replacement is happened.
Origin:
<img width="372" alt="origin" src="https://user-images.githubusercontent.com/47740690/230753301-c546bcce-2f02-410c-a047-f9a6f9d940ab.png">

In original problem, there are 256 frames. If there is no values which is not in page table page fault is occurred and put this page number and frame number to tlb and page table.  
FIFO:
<img width="370" alt="FIFO" src="https://user-images.githubusercontent.com/47740690/230753309-5f34c3cf-31c2-410b-8bb9-78594ff6c17a.png">

FIFO uses queue to manage page. If the page is full, page replacement is implemented so that victim for FIFO is selected. In FIFO, 538 pages replacement is happened. The reason why page fault is doubled is frame number decrease by 128 so that page replacement is often occurred by selecting victim. In FIFO algorithm, it just selects victim which is placed in header of queue.

LRU:
The LRU algorithm uses stack as data structure to store page table. Similarly, FIFO the LRU puts page number and frame number to page table and TLB until list is full. After the queue is full, LRU algorithm selects victim. The victim is selected in header in the list. The difference is that in this algorithm if page number is already in page remove this page and puts stack again.
<img width="408" alt="LRU" src="https://user-images.githubusercontent.com/47740690/230753315-c263ebee-a633-488e-a363-6b8bd585886f.png">

Result Analyzation
As we can see above results, page fault happens 538, and 539 respectively. In general, LRU happens less page fault because of consideration of locality. However, in this projects number is random which does not consider locality.

Implementation
Code explanation

1. If user takes input 0
   <img width="303" alt="input0" src="https://user-images.githubusercontent.com/47740690/230753327-f06a2300-3cb1-465f-a79d-6c5d3739dccd.png">
   Frame size is 256, so page replacement does not occur, so it assigns new frame for corresponding page_number and add frame to physical memory.

2.If user takes input 1

<img width="382" alt="input1" src="https://user-images.githubusercontent.com/47740690/230753354-fc70d50d-e798-4455-9f08-06e7a3cdd7f7.png">
If user takes input 1, it executes page replacement with FIFO algorithm. FIFO algorithm selects victim if there are more than 128 inputs like as below.
<img width="320" alt="input1-1" src="https://user-images.githubusercontent.com/47740690/230753374-0b0015ce-c466-4507-a50c-8493a6fce34a.png">

Victim is selected in front of queue and rest table[victim].frameNumber=-1 and table[victim].valid=false. After reset, it updates page table, TLB and replacedPage list.
If size is less than 128, it allocate in physical memory and call FIFO_add to add in page table
<img width="385" alt="input1-2" src="https://user-images.githubusercontent.com/47740690/230753375-f0f9e76c-1d8e-4d25-8a28-306a6bd9d3cf.png">

3.If user takes input 2,
<img width="452" alt="input2" src="https://user-images.githubusercontent.com/47740690/230753452-d4e19f44-44a5-450a-8262-7c8d452c4943.png">

Unlikely FIFO algorithm, LRU algorithm has to update list. If page is already in list, remove corresponding value and add to the top.
<img width="226" alt="input2-1" src="https://user-images.githubusercontent.com/47740690/230753467-4ee85cf1-96f0-4b98-b438-1da26b1888ff.png">
<img width="401" alt="input2-2" src="https://user-images.githubusercontent.com/47740690/230753476-4d7a2d08-3291-4d90-ac7a-37330885b952.png">
The LRU selects in front of value as a victim and likely FIFO, reset its frame number and validation.
<img width="323" alt="input2-3" src="https://user-images.githubusercontent.com/47740690/230753478-b2c78616-54f9-4057-801f-a9c68ea2bd20.png">
If frame is less than 128, it adds page_number without page replacement.
<img width="374" alt="input2-4" src="https://user-images.githubusercontent.com/47740690/230753494-666c4212-c85a-46e2-86e1-65ad3fa37295.png">

Error handling:
In this program, it takes input from user. Input should be range from 0 to 2 otherwise program consider input invalid and print error message and take input again.
<img width="390" alt="error_handling" src="https://user-images.githubusercontent.com/47740690/230753530-83e02f65-53b0-43cd-b5c9-7aea4f4c2b14.png">
Test Description and Results

Test1: Input:0
Input:
<img width="338" alt="error-1" src="https://user-images.githubusercontent.com/47740690/230753618-e6cc6f81-9529-4d7a-a617-2663e193c4f2.png">
Output:
TLB hit rate= 5.5% (55 happens)
Page fault rate 24.4% (244 happens)
<img width="372" alt="error-2" src="https://user-images.githubusercontent.com/47740690/230753622-d44ad1ba-a465-4e5b-8361-ac13bb642459.png">
We can see there is no page replacement,
<img width="432" alt="error-3" src="https://user-images.githubusercontent.com/47740690/230753793-10aece38-1d28-4cd6-a17d-48f64bf3e208.png">

Test 2
Input: 1 (page replacement with FIFO)
<img width="353" alt="error-4" src="https://user-images.githubusercontent.com/47740690/230753721-8f7a4d5b-5842-4ee7-8ca8-e4293768380c.png">
Output:
TLB hit rate 5.3%( 53 happen), page fault rate: 53.8% (538 happen)
The below image shows which page is replaced during executions.
<img width="452" alt="error-5" src="https://user-images.githubusercontent.com/47740690/230753723-e3e2c26f-3065-4b3a-82c4-f7e511041247.png">

<img width="370" alt="error-6" src="https://user-images.githubusercontent.com/47740690/230753666-f6a5b5bf-9143-4b95-b6e7-dde3d31ffd3a.png">
We can see replacement and reference on the command line.
<img width="418" alt="error-7" src="https://user-images.githubusercontent.com/47740690/230753667-97a34f76-0fde-4202-94cf-2101540af652.png">
Test 3
Input: 3 (Page replacement with LRU algorithm)
<img width="337" alt="error-8" src="https://user-images.githubusercontent.com/47740690/230753668-d05b645f-1e87-4d22-bcf0-a9d21d728879.png">
Output
TLG hit rate: 5.7%(57 times happen) Page fault rate 53.9%(539 times happen)
The below image shows which page is replaced during executions.
<img width="452" alt="error-9" src="https://user-images.githubusercontent.com/47740690/230753670-ca5f0728-bd57-4a65-a5dd-541cbffb887c.png">
<img width="408" alt="error-10" src="https://user-images.githubusercontent.com/47740690/230753671-13d18246-e80a-47d4-add3-0a6a7144f7c3.png">
Also, as same as FIFO algorithm, if frame is needed to be replacement, program prints out which frame is reallocated to page.
<img width="415" alt="error-11" src="https://user-images.githubusercontent.com/47740690/230753672-1f22c7ff-b88e-4f16-b115-d467d9dd3e9b.png">

[Introduction]: The program being executed must be in main memory. However, in many cases, the entire program in not necessary, so by introducing virtual memory, it has benefits from virtual memory is that programs can be larger than physical memory and abstracts main memory into large, uniform array of storage so that it can implement multi programming and increase CPU utilization. Further it requires less I/O or swapping programs.
[Process]: If there is reference page before, it refers page table and mapped to physical memory using page number and offset. Otherwise, if the bit is set to invalid, it causes trap to operating system and store its current state. Operating system check internal table to determine the reference was a valid or an invalid memory access. If it is invalid, terminate the process. If it is valid operating system schedule secondary storage operation to bring desired page into newly allocated frame. After it is completed, operating system modifies page table to indicate the page is now in memory and restore instructed instruction.

[Analyzing code]
[AddressTranslator]
<img width="452" alt="addresstranslator" src="https://user-images.githubusercontent.com/47740690/230753878-e50c9869-12b4-40fb-a91e-3801856d492a.png">
<img width="452" alt="addresstranslator-1" src="https://user-images.githubusercontent.com/47740690/230753881-21fe6753-fb7b-492c-a410-9f9f7862c635.png">

[PageTable]
<img width="303" alt="pt-1" src="https://user-images.githubusercontent.com/47740690/230753889-dce4d2f5-bb8a-4b59-98b2-698a334802ae.png">
The page table constructor allocate page table array and initiates it value by -1 and false.
<img width="298" alt="pt-2" src="https://user-images.githubusercontent.com/47740690/230753892-bd524cfa-264f-47f6-91db-6ebe470d964f.png">
The get(int p_num) method is accessed by other class by sending parameter p_num(parameter number). If there is corresponding frame number, return frame number otherwise returns -1.
<img width="378" alt="pt-3" src="https://user-images.githubusercontent.com/47740690/230753893-2e44b9a3-5d83-4aee-96b7-7d7ebfc7fe96.png">
The add method adds page number and frame number in table

[PageTableItem]
<img width="288" alt="pt-4" src="https://user-images.githubusercontent.com/47740690/230753894-07a98fc7-96f5-4335-a712-d7e87eb84afc.png">
The page table item stores its frame number and valid information.

[TLB]
<img width="452" alt="tlb" src="https://user-images.githubusercontent.com/47740690/230753930-19d28c8b-da19-412a-be3c-918568de58dc.png">
[BackStore]
In the BackStore, declare its value and result type and find corresponding real data in backstore.
<img width="452" alt="bs-1" src="https://user-images.githubusercontent.com/47740690/230753934-cf07a84d-9e94-4f96-b3af-87cd35bce605.png">
<img width="452" alt="bs-2" src="https://user-images.githubusercontent.com/47740690/230753936-3b208a3e-e943-4924-b3dc-a4b68d875adf.png">
[Frame]
The addFrame method returns frame number.
<img width="452" alt="frame-1" src="https://user-images.githubusercontent.com/47740690/230753941-17fe2555-b2bf-40ee-a895-9fcae6fe02fb.png">
The int getValue returns corresponding value in frame.
<img width="330" alt="frame-2" src="https://user-images.githubusercontent.com/47740690/230753943-faa561ad-ded2-4c1b-b9de-461e7bfbdaf5.png">
