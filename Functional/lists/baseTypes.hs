import           Data.List

toDouble :: Integer -> Double
toDouble x  = fromIntegral x :: Double

toFloat :: Int -> Float
toFloat x = fromIntegral (toInteger x) :: Float

recFib :: (Int, Int, Int) -> Int
recFib (no, curr, prev)
  | no == 0 = prev
  | otherwise = recFib (no-1, curr + prev, curr)

linearFib :: Int -> Int
linearFib n = recFib (n, 1, 0)

middle :: Ord a => (a,a,a) -> a
middle (a, b, c) = sort[a, b, c]!!1

removeX :: Eq a => a -> [a] -> [a]
removeX _ [] = []
removeX x (y:ys)
  | x == y = ys
  | otherwise = y : removeX x ys

quick ::Ord a => [a] -> [a]
quick [] = []
quick n = quick (filter (<= pivot) ns) ++ [pivot] ++ quick (filter( > pivot) ns)
  where
    size = length n `div` 2
    pivot = middle(head n, last n, n!!size)
    ns = removeX pivot n

inites::Eq a => [a] -> [[a]]
inites list = [take k list | k<-[0..length list]]

sublists ::Eq a => [a] -> [[a]]
sublists []     = [[]]
sublists (x:xs) = [x:sublist | sublist <- sublists xs] ++ sublists xs

partitions::Eq a => [a] -> [([a], [a])]
partitions xs = [(ys, zs) |ys <- sublists xs, zs <- sublists  xs, xs == ys ++ zs]

nubs::Eq a => [a] -> [a]
nubs [] = []
nubs (x:xs)
  | x `elem` xs = nubs xs
  | otherwise = x : nubs xs

permut::[Int] -> [[Int]]
permut [] = [[]]
-- i didn't find out an algorithm
permut xs = nubs $ permutations xs


howManyNumbers::Int -> Int -> Int
howManyNumbers n x
  | mod n x == 0 = 1 +  howManyNumbers (div n x) x
  | otherwise = 0

decimalCount::Int -> Int
decimalCount n = min
  (sum [howManyNumbers k 2 | k<-[1..n]]) (sum [howManyNumbers k 5 | k<-[1..n]])

main :: IO()
main = do
  print (toFloat 15)
  print (toDouble 13)
  print (linearFib 5)
  print (middle (4, 3, 5))
  print (quick [1, 3, 5, 7, 2, 4, 6, 8, 0])
  print (inites [1, 2, 3, 4, 5, 6])
  print (partitions [1, 2, 3, 4])
  print (nubs [1, 1, 1, 1, 2, 1, 2, 4, 2])
  print (permut [1, 2, 1])
  print (decimalCount 25)
