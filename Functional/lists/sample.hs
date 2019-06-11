import qualified Control.Monad.Trans.Writer.Lazy as W
import Data.List

-- sub :: Int -> Int -> [[Int]]
-- sub n k =

solution :: Int -> Int -> [[Int]]
solution n k | k == 1 = make [] n | otherwise = concatMap (`make` n) $ solution n (k-1)
  where
    make [] maxdig = [[a] | a <- [1..maxdig]]
    make starting_seq maxdig = [starting_seq ++ [a] | a <- [last starting_seq + 1..maxdig]]


-- npd' n k = if (n `mod` k == 0) then k else npd' n (k+1)
-- npd n = npd' n 2
--
-- dzp :: Integer -> [Integer]
-- dzp n = if n == 1
--     then
--         []
--     else
--         let
--             p = npd n
--         in
--             p:(dzp (div n p))
-- omega n = length . nub$dzp n
--
-- sip n = (fromIntegral(sum$fmap omega [2..n])) / (fromIntegral(n-1))



omega :: Int -> Float
omega n = (fromIntegral (sum list) :: Float) / (fromIntegral (length list) :: Float)
  where
    list = [omega' k | k<-[2.. n]]
    dividers p = length [k | k<-[2..(div p 2)], mod p k == 0]
    omega' x
      | dividers x == 0 = 1
      | otherwise = length [d | d<-[2..(div x 2)], mod x d == 0, dividers d == 0]


myGCD :: Int -> Int -> W.Writer [String] Int
myGCD x y = do
  rest <- W.writer (mod x y,  [show x ++ " mod " ++ show y])
  if rest == 0
    then return y
    else myGCD y rest


main :: IO()
main = do
  print (solution 10 3)
  print (myGCD 28 12)
  print (omega 11)
