global main
extern printf


section .data
  print_format:
        db      "%i is prime", 10, 0


section .text
  main:
      ;set up stack frame
        push    ebp
        mov     ebp, esp
        pushad
      ;set up loop
        mov     edi, 1
        ;jmp     main_loop

  main_loop:
        add     edi, 1          ;edi <- iterator main_loop
        cmp     edi, 10000
        jle     is_prime_fun
        ;jmp     exit_fun

  exit_fun:
        mov     ebx, 0
        mov     eax, 1
        int     0x80

  print:
        push    ebx
        push    print_format
        call    printf
        add     esp, 8
        jmp     main_loop

  is_prime_fun:
        mov     ebx, edi
        mov     eax, ebx        ;ebx <- checking value
        mov     esi, 2          ;esi <- prime loop iterator
        ;jmp     prime_loop
        
  prime_loop:
        cmp     esi, ebx        ;if esi < ebx go forward
        jge     is_prime        ;if checking value has not divisors > 1 and < himself that mean ebx is prime
        mov     eax, ebx
        mov     edx, 0
        div     esi
        cmp     edx, 0          ;if edx == 0, then ebx(our value) is divisible by esi, end ebx is not prime
        je      main_loop
        add     esi, 1
        jmp     prime_loop

  is_prime:
        mov     eax, 0
        mov     edx, 1        
        jmp     print

        
        








