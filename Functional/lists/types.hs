m91 :: Int -> Int
m91 x | x > 100 = x - 10 | otherwise = m91(m91 (x+11))

m91result :: Int -> [Int]
m91result n = [m91 k | k<-[0..n]]

data IntOrString  = String | Int deriving Eq

data BTree a = L a | N a (BTree a) (BTree a) deriving Eq

instance (Show a) => Show (BTree a) where
  show (L x) = "<" ++ show x ++ ">"
  show (N l x r) = "[" ++ show l ++ ", " ++ show x ++ ", " ++ show r ++ "]"

-- instance Eq IntOrString   where
--   x == y =  integerEq x y

-- showIntOrString :: [IntOrString] -> Int
-- showIntOrString = length

main :: IO()
main = do
  print (m91result 101)
  let tree = N 12 (L 3) (N 1 (L 1) (L 3))
  print tree
