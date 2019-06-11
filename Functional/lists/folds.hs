evens :: [Int] -> Int
evens = foldr f 0
  where
    f a b
      | mod a 2 == 0 = b + 1
      | otherwise = b

approx ::Int -> Double
approx n =
   foldl (\a b ->  a + (1/(fromIntegral (product [1..b]) :: Double))) 0 [1..n]


reverseSum ::Num a => [a] -> a
reverseSum xs | mod (length xs) 2 == 0 =  (-1)*fold | otherwise = fold
  where
    fold = foldl (\a b ->((-1)*a + b)) 0 xs

average :: Fractional a => [a] -> (a, a)
average xs = (avg, foldl f 0 xs/size)
  where
    size = fromIntegral (length xs)
    avg = sum xs/size
    f a b = a + (b - avg)^2

main :: IO()
main = do
  print (evens [1, 2, 3, 4, 6, 3])
  print (approx 4)
  print (reverseSum [17, 2, 3, 19])
  print (average [1, 2, 3])
