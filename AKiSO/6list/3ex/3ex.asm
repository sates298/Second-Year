global  main
extern  printf, scanf


section .data
  print_format:
        db      "%lf", 0xA, 0
  scan_format:
        db      "%lf %c %lf", 0

section .bss
  wanted_operation:
        resb    1
  first:
        resq    1
  second:
        resq    1
  result:
        resq    1
        

section .text
  main:
      ;set up stack frame
        push    ebp
        mov     ebp, esp
        pushad

      ;get line from user
        call    scan

      ;find right operation  
        cmp     byte [wanted_operation], '+'
        je      add_fun
        cmp     byte [wanted_operation], '*'
        je      mul_fun
        cmp     byte [wanted_operation], '/'
        je      div_fun
        cmp     byte [wanted_operation], '-'
        je      sub_fun
      ;else end program
        jmp     end_program


  end_program:  
      ;exit(0)
        mov     ebx, 0
        mov     eax, 1
        int     0x80
       

  print:
        push    dword [result+4]
        push    dword [result]
        push    print_format
        call    printf
        jmp     end_program

  scan:
        push    second
        push    wanted_operation
        push    first
        push    scan_format
        call    scanf
        add     esp, 16
        ret

  add_fun:
        finit
        fld     qword [first] 
        fadd    qword [second]
        fstp    qword [result]
        jmp     print
        
  mul_fun:
        finit
        fld     qword [first]
        fmul    qword [second]
        fstp    qword [result]
        jmp     print

  div_fun:
        finit
        fld     qword [first]
        fdiv    qword [second]
        fstp    qword [result]
        jmp     print

  sub_fun:
        finit
        fld     qword [first]
        fsub    qword [second]
        fstp    qword [result]
        jmp     print







        
        
