Create View average As
Select AvG(payment.amount) as average_price
From payment
Where payment.payment_date Like '2005-07-07%';

Create view quantity As
Select payment.customer_id, Avg(payment.amount) as prices_average
From payment
Group by payment.customer_id;

Select customer.customer_id, customer.first_name, customer.last_name
From payment Join customer On customer.customer_id = payment.customer_id
	Join quantity On quantity.customer_id = payment.customer_id
Where  quantity.prices_average > (Select average_price From average)
Group By customer.customer_id, customer.first_name, customer.last_name;

Drop View average;
Drop View quantity;