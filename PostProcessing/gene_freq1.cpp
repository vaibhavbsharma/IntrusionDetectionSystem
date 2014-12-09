#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include <istream>
using namespace std;

int main() {
  ifstream fin("GA1-50-runs.txt");
  string raw_line;
  int count=0;
  int generation=0;
  vector<float> gene_count(41,0.0);
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
	string tmp=results[1];
	//cout<<tmp<<endl;
    for(int j=0;j<tmp.length();j++) {
        if(tmp[j]=='1') gene_count[j] = gene_count[j]+1.0;
    }
    if(count==20) {
      count=0;
	  generation++;
      for(int i=0;i<gene_count.size() && generation%10==0;i++) {
        cout<<(gene_count[i]/(generation*20))<<" ";
	  }
      if(generation%10==0) cout<<endl;
    }
  }
  return 0;
}
