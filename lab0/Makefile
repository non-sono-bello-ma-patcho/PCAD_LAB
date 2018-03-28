E1=es1
E2=es2
PC=prod_cons
BIN=bin
PFLAG= -pthread -o
all: ${E1}.out ${E2}.out ${PC}.out

# easily compile each file:
${PC}.out: ${PC}.c | ${BIN}
	gcc ${PFLAG} ${BIN}/$@ $< 

${E1}.out: ${E1}.c | ${BIN}
	gcc ${PFLAG} ${BIN}/$@ $< 

${E2}.out: ${E2}.c | ${BIN}
	gcc ${PFLAG} ${BIN}/$@ $< 
