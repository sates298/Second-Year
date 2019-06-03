euler :: Int -> Int
euler n = length [k | k <- [1..n],  gcd k n == 1 ]

eulerSum :: Int -> Int
eulerSum n = sum [euler k | k<-[1..n], mod n k == 0]

trinity :: Integer -> [(Integer,  Integer,  Integer)]
trinity n = [(a, b, c) | a<-[1..n], b<-[1..a], c <-[1..b] , a^2 == b^2 + c^2, gcd b c == 1]

fib1 :: Int -> Int
fib1 1 = 1
fib1 0 = 0
fib1 n = fib1 (n-1) + fib1 (n-2)

fib2 :: Int -> Int
fib2 n | n < 1 = 0 | n == 1 = 1 | otherwise = fib2 (n-1) + fib2 (n-2)

fibList :: Int -> [Int]
fibList n = [fib2 i | i <- [1..n]]

newton :: Int -> Int -> Int
newton n k
  | n < k = 0
  | k < 0 = 0
  | k == 0 = 1
  | k == n = 1
  | otherwise = newton (n-1) k + newton (n-1) (k-1)

perfect :: Int -> [Int]
perfect n = [k | k<-[2..n], k == sum [i | i<-[1 .. k-1], mod k i == 0]]

main :: IO()
main = do
  print (euler 11)
  print (eulerSum 6)
  print (trinity 200)
  print (fib1 5)
  print (fib2 5)
  print (fibList 5)
  print (newton 4 2)
  print (perfect 10000)
