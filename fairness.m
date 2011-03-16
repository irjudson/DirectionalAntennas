figure(4);
X = [ 8, 10, 12, 14 ];
SSPOPTFI = [ 0.88141026, 0.75021552, 0.61563126, 0.788885555 ];
SSPOPTFO = [ 0.84887129, 0.68523622, 0.81012658, 0.83902059  ];
SSPLPRFI = [ 0.92406075, 0.79655781, 0.74167543, 0.8581936   ];
SSPLPRFO = [ 0.818696884, 0.751457119, 0.788522452, 0.917524808 ];
SSPMSTFI = [ 0.890779557, 0.910619469, 0.75, 0.824943897 ];
SSPMSTFO = [ 0.897543901, 0.960827034, 0.76119403, 0.8527529 ];
SSPkNNFI = [ 0.925954493, 0.899750934, 0.766077705, 0.909281895 ];
SSPkNNFO = [ 0.933151963, 0.888683887, 0.779404658, 0.951988447 ];
SSPOPT = SSPOPTFI + SSPOPTFO
SSPLPR = SSPLPRFI + SSPLPRFO
SSPMST = SSPMSTFI + SSPMSTFO
SSPkNN = SSPkNNFI + SSPkNNFO
plot(X,SSPOPT,'--og ','LineWidth',3);
hold on;
plot(X,SSPLPR,':*b','LineWidth',3);
plot(X,SSPMST,'-.sc','LineWidth',3);
plot(X,SSPkNN,'-+r','LineWidth',3);
xlabel('Number of Nodes');
ylabel("Jain's Fairness");
legend('SSP-OPT','SSP-LPR','SSP-MST', 'SSP-kNN',  "Location", "NorthEast")
set(gca, 'XTickMode', 'manual', 'XTick', X);
hold off;
fixAxes;
print(4, "fairness.eps", "-deps");
print(4, "fairness.pdf", "-dpdf");
