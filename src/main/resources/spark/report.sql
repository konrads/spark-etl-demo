SELECT (SELECT count(1) FROM client_spending) AS client_spending_count,
       (SELECT count(1) FROM item_purchase)   AS item_purchase_count,
       (SELECT count(1) FROM minor_purchase)  AS minor_purchase_count