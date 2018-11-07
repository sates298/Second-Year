Create View v1 As 
	Select distinct c.first_name, c.last_name
	From customer As c Inner Join rental On c.customer_id = rental.customer_id
	Where rental.staff_id = '1';
Create View v2 As
	Select distinct c.first_name, c.last_name
	From customer As c Inner Join rental On c.customer_id = rental.customer_id
	Where rental.staff_id = '2' ;
Select distinct v1.first_name, v1.last_name
From v1 Inner Join v2 On v1.first_name Like v2.first_name 
Where v1.last_name Like v2.last_name;

Drop View v1;
Drop View v2;
       

