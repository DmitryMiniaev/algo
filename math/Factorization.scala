object Main {
    def factorize(n: Int): List[Int] = {
      val range = 1 to sqrt(n).asInstanceOf[Int]
      range.foldLeft(List[Int]()) { (xs: List[Int], i: Int) =>
        if (n % i == 0) {
          if (i * i != n) i :: n / i :: xs
          else i :: xs
        } else xs
      }
    }    
}