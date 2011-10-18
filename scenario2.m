figure(2);
X = [ 10, 15, 20, 25, 30 ];

% data from first run
%SSPLPR = [ 1.02e+09, 2.30e+09, 3.97e+09, 5.63e+09, 7.79e+09 ];
%SSPMST = [ 7.22e+08, 1.32e+09, 2.11e+09  2.80e+09, 3.74e+09 ];
%SSPkNN = [ 9.84e+08, 2.05e+09, 3.34e+09, 4.59e+09, 6.01e+09 ];

% data from new run

SSPLPR = [ 1.08E+09, 2.30E+09, 3.97E+09, 5.64E+09, 7.79E+09 ];
SSPMST = [ 7.39E+08, 1.33E+09, 2.11E+09, 2.81E+09, 3.74E+09 ];
SSPkNN = [ 1.02E+09, 2.05E+09, 3.34E+09, 4.59E+09, 6.02E+09 ];
SSPMSTP = [ 1.08E+09, 2.31E+09, 3.97E+09, 5.64E+09, 7.77E+09 ];

SSPLPR = SSPLPR / 1000000;
SSPMST = SSPMST / 1000000;
SSPMSTP = SSPMSTP / 1000000;
SSPkNN = SSPkNN / 1000000;

plot(X,SSPLPR,':*b','LineWidth',3);
hold on;
plot(X,SSPMST,'-.sc','LineWidth',3);
plot(X,SSPkNN,'--og ','LineWidth',3);
plot(X,SSPMSTP, ':xm', 'LineWidth', 3);

xlabel('Number of Nodes');
ylabel('Total Capacity (Mbps)');
legend('SSP-LPR','SSP-MST','SSP-kNN', 'SSP-MSTPlus', "Location", "NorthWest");
hold off;
fixAxes;
print(2, "scenario2.eps", "-deps");
print(2, "scenario2.pdf", "-dpdf");
