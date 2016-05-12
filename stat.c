#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/stat.h>
#include <pwd.h>
#include <time.h>
int main(){
  struct stat st;
  stat("a.out", &st);
  printf("%d\n", st.st_size);
  printf("%d\n", st.st_uid);
  struct passwd *pwd;
  pwd = getpwuid(st.st_uid);
  printf("%s\n", pwd->pw_name);
  //getpwuid - ot id kum username na potrebitelq
  //getgrid - ot id vru6ta grupata
  //localtime

  printf("%d\n", st.st_mtime);
  struct tm *t = localtime(&st.st_mtime);
  printf("%d\n", t->tm_min);

  //getopt -

  printf("%d\n", st.st_mode & S_IXUSR);
  //if(0) -> ne e vdignat if(!= 0) => vdignat e   
}
