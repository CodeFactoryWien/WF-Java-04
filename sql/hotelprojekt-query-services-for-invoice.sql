SELECT servicesID, serviceType, serviceDate,
 	coalesce(serv_movies.moviePrice, serv_wellness.wellnessPrice, serv_minibar.mbPrice) as pps,  
    COUNT(coalesce(serv_movies.movieName, serv_wellness.wellnessName, serv_minibar.mbItem)) as quant, 
	coalesce(serv_movies.movieName, serv_wellness.wellnessName, serv_minibar.mbItem) AS serviceName
FROM services
LEFT OUTER JOIN serv_movies ON (services.fk_serviceID = serv_movies.movieID AND services.serviceType = 'movie') 
LEFT OUTER JOIN serv_wellness ON (services.fk_serviceID = serv_wellness.wellnessID AND services.serviceType = 'wellness') 
LEFT OUTER JOIN serv_minibar ON (services.fk_serviceID = serv_minibar.mbID AND services.serviceType = 'minibar')
WHERE fk_bookingID=3
GROUP BY serv_minibar.mbItem, serv_movies.movieName, serv_wellness.wellnessName
HAVING COUNT(*) > 0;
