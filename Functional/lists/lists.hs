import Control.Arrow

spam :: Ord a => (a -> Bool) -> [a] -> ([a], [a])
spam _ [] = ([], [])
spam p (x:xs) =  spam' p [] [] (x:xs)
   where
   spam' _ _ _ [] = ([], [])
   spam' f a b (y:ys)
      | f y = let (aa, bb) = spam' f a b ys in (y : aa, bb)
      | otherwise = ([], y : ys)

ecd :: Eq a => [a] -> [a]
ecd [] = []
ecd (x:xs) = ecd' [] (x:xs)
  where
    ecd' a [] = a
    ecd' a (y:ys)
      | null a = ecd' [y] ys
      | last a == y = ecd' a ys
      | otherwise = ecd' (a ++ [y]) ys

pack :: Eq a => [a] -> [[a]]
pack [] = []
pack (x:xs) = pack' [] [x] xs
  where
    pack' acc curr [] = acc ++ [curr]
    pack' acc curr (y:ys)
      | head curr == y = pack' acc (y:curr) ys
      | otherwise = pack' (acc ++ [curr]) [y] ys

rleEncode ::Eq a => [a] -> [(Int, a)]
rleEncode text = map (length &&& head ) (pack text)

rleDecode :: [(Int, a)] -> [a]
rleDecode = concatMap (uncurry replicate)
-- uncurry replicate <==> \(x, c) -> replicate x c

main :: IO()
main = do
  print (spam (> 5) [6, 7, 5, 6, 3, 2])
  print (ecd [1,1,1,2,2,1,1])
  print (pack [1,1,1,2,2,1,1,1,3,3])
  print (pack "aaaaaaccccaaaaaaaaabbb")
  print (rleEncode "aaaabbbbbbbaaaacccbbbbbbb")
  print (rleDecode [(4, 'a'), (5, 'c'), (6, 'b')])
