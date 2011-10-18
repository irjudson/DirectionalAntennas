figure(3);
X = [ 4, 6, 8, 10, 12 ];

% First run
%SSPLPR = [ 3.06e+09, 3.45e+09, 3.97e+09, 4.01e+09, 4.33e+09 ];
%SSPMST = [ 2.18e+09, 2.17e+09, 2.11e+09  2.03e+09, 3.01e+09 ];
%SSPkNN = [ 2.87e+09, 3.11e+09, 3.34e+09, 3.18e+09, 3.35e+09 ];

SSPLPR = [ 3.06E+09, 3.46E+09, 3.97E+09, 4.01E+09, 4.34E+09 ];
SSPMST = [ 2.18E+09, 2.17E+09, 2.11E+09, 2.03E+09, 2.02E+09 ];
SSPkNN = [ 2.87E+09, 3.11E+09, 3.34E+09, 3.18E+09, 3.35E+09 ];
SSPMSTP = [ 3.03E+09, 3.46E+09, 3.97E+09, 4.02E+09, 4.33E+09 ];

SSPLPR = SSPLPR / 1000000;
SSPMST = SSPMST / 1000000;
SSPMSTP = SSPMSTP / 1000000;
SSPkNN = SSPkNN / 1000000;

plot(X,SSPLPR,':*b','LineWidth',3);
hold on;
plot(X,SSPMST,'-.sc','LineWidth',3);
plot(X,SSPkNN,'--og ','LineWidth',3);
plot(X,SSPMSTP, ':xm', 'LineWidth', 3);

xlabel('Number of Sectors');
ylabel('Total Capacity (Mbps)');
legend('SSP-LPR','SSP-MST','SSP-kNN', 'SSP-MSTPlus', "Location", "NorthWest");
set(gca, 'XTickMode', 'manual', 'XTick', X);
hold off;
fixAxes;
print(3, "scenario3.eps", "-deps");
print(3, "scenario3.pdf", "-dpdf");
