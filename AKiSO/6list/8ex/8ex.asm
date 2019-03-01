global main
extern printf, scanf


section .data
  scan_format:
        db      "%lf %d", 0
  msg_result:
        db      "%lf", 10, 0
  msg_log:
        db      "sinh^-1(%lf) = ", 0
  msg_e:
        db      "sinh^1(%lf) = ", 0


section .bss
  x:    resq    4
  op:   resd    4
  temp: resq    4
  res:  resq    4


section .text
  main:
      ;set up stack frame
        push    ebp
        mov     ebp, esp
        pushad

        call    scan
        cmp     dword [op], 1
        je      e_chosen
        cmp     dword [op], 2
        je      log_chosen
        ;jmp     exit_fun
        
        
  exit_fun:
        mov     ebx, 0
        mov     eax, 1
        int     0x80

  scan:
        push    op
        push    x
        push    scan_format
        call    scanf
        add     esp, 12
        ret
 
  print_one:
        push    dword [x+4]
        push    dword [x]
        push    msg_e
        call    printf
        jmp     print_result
        

  print_two:
        push    dword [temp+4]
        push    dword [temp]
        push    msg_log
        call    printf
        jmp     print_result

  print_result:
        push    dword [res+4]
        push    dword [res]
        push    msg_result
        call    printf
        jmp     exit_fun

  e_chosen:
        finit
        call    pow_e_x
        call    pow_e_sign_x
        fld     qword [temp]   ;e^x
        fld     qword [res]    ;e^(-x)
        fsub    
        fstp    qword [res]    ;res = e^x - e^(-x)
        fld1
        fld1
        fadd                   ;st(0) = 1 + 1 = 2
        fstp    qword [temp]
        fld     qword [res]
        fld     qword [temp]
        fdiv
        fstp    qword [res]
        jmp     print_one
        
  log_chosen:
        finit
        fld     qword [x]
        fstp    qword [temp]
        fld     qword [x]
        fld     st0
        fmul                    ;x ** 2
        fld1                    ;load 1 
        fadd                    ;x**2 + 1
        fsqrt                   ;sqrt(x ** 2 + 1)        
        fld     qword [x]
        fadd                    ;x + sqrt( x**2 + 1) 
        fstp    qword [x]
        call    log_e_x           
        jmp     print_two

  pow_e_x:                      ; use e^x as 2^(x * log_2(e))
        fldl2e                  ;load log_2(e)
        fld     qword [x]
        fmul                    ;x * log_2(e)
        fld1
        fld     st1             ;duplicate x * log_2(e)
        fprem                   ;st(0) mod st(1) = (x * log_2(e)) mod 1 [later as "reminder"]
        f2xm1                   ;2^(reminder) - 1
        faddp   st1, st0        ;previous line + 1 = 2^(reminder)
        fscale                  ;st(0) * 2^(st(1)) = 2^(reminder) * 2^(floor(x * log_2(e))
        fstp    qword [temp]    ;result from st(0) store in temp variable
        ret

  pow_e_sign_x:
        fldl2e                  ;load log_2(e)
        fld     qword [x]
        fmul                    ;x * log_2(e)
        fchs                    ;st(0) = -(x * log_2(e)) 
        fld1
        fld     st1             ;duplicate -(x * log_2(e))
        fprem                   ;st(0) mod st(1) = -(x * log_2(e)) mod 1 [later as "-reminder"]
        f2xm1                   ;2^(-reminder) - 1
        faddp   st1, st0        ;previous line + 1 = 2^(-reminder)
        fscale                  ;st(0) * 2^(st(1)) = 2^(-reminder) * 2^(floor(-(x * log_2(e)))
        fstp    qword [res]     ;result from st(0) store in "result" variable
        ret

  log_e_x:
        fldln2                  ;load log_e(2)
        fld     qword [x]
        fyl2x                   ;compute log_e(2)*log_2(x) = log_e (x)
        fstp    qword [res]     ;save result
        ret





        
