#include <stdio.h>
#include <sys/types.h>
#include <dirent.h>
int main(){

  DIR *dir = opendir(".");
  struct dirent *entry;
  while((entry = readdir(dir)) != NULL){
    printf("%s\n", entry->d_name);
  }
  closedir(dir);
}
