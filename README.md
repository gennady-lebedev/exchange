Testcase `Discrete Auction`.
Task:
* collect buy and sell orders
* calculate `optimal price` - single price to maximize amount of lots;
* buyers will buy if optimal price equals or less than the order price
* sellers will sell if optimal price equals or more than the optimal price
* orders should be entered via standard input in format `<direction> <amount> <price>` like `S 42 10.00`, `B 1000 100.00`
* order price limited between 1 and 100, precision 2 digits after decimal point
* order amount limited between 1 and 1000
* total amount of orders limited by 1M
* output will be formated `<amount> <optimal price>`, like `100500 42.00`
* if no deals available with entered orders - output will be `0 n/a`
* if entered orders allows 2+ optimal prices, average value will be used

To run project use `gradle run`, then enter orders. Enter `exchange` to calculate optimal price.
Enter `exit` or `quit` to stop the programm.
