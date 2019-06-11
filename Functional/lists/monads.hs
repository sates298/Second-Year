import qualified Control.Monad.Trans.Writer.Lazy as W

data Term = V Int | Plus Term Term | Mult Term Term| Div Term Term

eval :: Term -> Maybe Int
eval (V x) = Just x
eval (Plus x y) = do
  a <- eval x
  b <- eval y
  return (a + b)
eval (Mult x y) = do
  a <- eval x
  b <- eval y
  return (a * b)
eval (Div x y) = do
  a <- eval x
  b <- eval y
  if b==0
    then Nothing
    else return (div a b)

myGCD :: Int -> Int -> W.Writer [String] Int
myGCD x y = do
  rest <- W.writer (mod x y,  [show x ++ " mod " ++ show y])
  if rest == 0
    then return y
    else myGCD y rest

main :: IO()
main = do
  let term = Mult (V 2) (Plus (V 5) (Div (V 4) (V 2)))
  print (eval term)
  print (myGCD 12 42)
