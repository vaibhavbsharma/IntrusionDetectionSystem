#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include <istream>
using namespace std;

int main() {
  ifstream fin("GA2-50-runs.txt");
  string raw_line;
  vector<double> v_d;
  vector<string> v_s;
  int count=0;
  int generation=0;
  while(getline(fin,raw_line)) {
    //cout<<raw_line<<"_"<<endl;
    count++;
    double d;
    stringstream ss(raw_line.c_str());
    string candidate_str;
    istream_iterator<string> it(ss);
    istream_iterator<string> end;
    vector<string> results(it, end);
    //cout<<results[0]<<"_"<<results[1]<<endl;
    //double kappa=stod(results[0]);
    //candidate_str=results[1];
    //cout<<kappa<<"_"<<candidate_str<<endl;
    v_d.push_back(stod(results[0]));
    v_s.push_back(results[1]);
    if(count==20) {
      count=0;
      double max=0;
      double avg=0;
      for(int i=0;i<v_d.size();i++) {
        if(v_d[i]>max) max=v_d[i];
        avg+=v_d[i];
      }
      generation++;
      //cout<<generation<<" "<<max*10000<<endl;
      cout<<generation<<" "<<(avg/20)*10000<<endl;
      v_d = vector<double>();
    }
  }
  return 0;
}
