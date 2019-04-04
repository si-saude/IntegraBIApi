package br.com.saude.api.generic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.javatuples.Triplet;

import br.com.saude.api.util.constant.TypeFilter;

public class Helper {
	private static StringBuilder stringBuilder;
	
	public static String filterLike(String str) {
		str = str.replace("á", "%").replace("é", "%").replace("í", "%").replace("ó", "%").replace("ú", "%")
				.replace("Á", "%").replace("É", "%").replace("Í", "%").replace("Ó", "%").replace("Ú", "%")
				.replace("À", "%").replace("à", "%")
				.replace("ã", "%").replace("õ", "%").replace("Ã", "%").replace("Õ", "%")
				.replace("â", "%").replace("ê", "%").replace("ô", "%")
				.replace("Â", "%").replace("Ê", "%").replace("Ô", "%")
				.replace("ç", "%").replace("Ç", "%");
				
		
		stringBuilder = new StringBuilder("%");
		stringBuilder.append(str);
		stringBuilder.append("%");
		return stringBuilder.toString();
	}
	
	// @SuppressWarnings("deprecation")
	public static long getNow() {
		Date now = new Date();
		// now.setHours(now.getHours() - (now.getTimezoneOffset()/60));
		return getTime(now);
	}
	
	@SuppressWarnings("deprecation")
	public static long getToday() {
		Date today = new Date();
		today.setHours(0 /*- (today.getTimezoneOffset()/60)*/);
		today.setMinutes(0);
		return getTime(today);
	}
	
	@SuppressWarnings("deprecation")
	private static long getTime(Date date) {
		date.setSeconds(0);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime().getTime();
	}
	
	public static String toStringDate(long longDate) {
		DateFormat simple = new SimpleDateFormat("dd/MM/yyyy"); 
		Date date = new Date(longDate);
		return simple.format(date);
	}
	
	public static long addDays(long day, int add) {
		return day + (1000*60*60*24*add);
	}
	
	public static Criterion getCriterionDateFilter(String propertyName, DateFilter dateFilter) {
		if(dateFilter != null && dateFilter.getTypeFilter() != null && dateFilter.getInicio() > 0) {
			switch(dateFilter.getTypeFilter()) {
				case TypeFilter.ENTRE:
					return Restrictions.between(propertyName, 
								dateFilter.getInicio(), 
								dateFilter.getFim());
			case TypeFilter.MAIOR_IGUAL:return andDifferentZero(propertyName,Restrictions.ge(propertyName, dateFilter.getInicio()));
			case TypeFilter.MAIOR:return andDifferentZero(propertyName,Restrictions.gt(propertyName, dateFilter.getInicio()));
			case TypeFilter.MENOR_IGUAL:return andDifferentZero(propertyName,Restrictions.le(propertyName, dateFilter.getInicio()));
			case TypeFilter.MENOR: return andDifferentZero(propertyName,Restrictions.lt(propertyName, dateFilter.getInicio()));
			case TypeFilter.IGUAL: return andDifferentZero(propertyName,Restrictions.eq(propertyName, dateFilter.getInicio()));
			case TypeFilter.DIFERENTE: return andDifferentZero(propertyName,Restrictions.ne(propertyName, dateFilter.getInicio()));
			default:
				return null;
			}
		}
		
		return null;
	}
	
	private static Criterion andDifferentZero(String propertyName, Criterion c) {
		return Restrictions.and(Restrictions.ne(propertyName, (long)0), c);
	}

	public static String getStringMonth(int month) {
		String[] months = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };
		return months[month];
	}
	
	public static String getStringDiaSemana(int diaSemana) {
		String[] dias = { "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };
		return dias[diaSemana];
	}

	public static Criteria loopCriterias(Criteria criteria, List<Triplet<String,CriteriaExample,JoinType>> criterias, Map<String,String> aliases) {
		if(aliases == null)
			aliases = new HashMap<String,String>();
		if(criterias != null) {
			for(Triplet<String,CriteriaExample,JoinType> c: criterias) {
				String alias = getNextAlias(aliases, c.getValue0(),0);
				aliases.put(alias, alias);
				Criteria example = criteria.createCriteria(c.getValue0(),alias,c.getValue2());
				for(Criterion criterion : c.getValue1().getCriterions())
					example.add(criterion);
				example.add(c.getValue1().getExample());
				
				example = loopCriterias(example, c.getValue1().getCriterias(),aliases);
			}
		}
		return criteria;
	}
	
	private static String getNextAlias(Map<String,String> aliases, String alias, int x) {
		String newAlias = alias;
		if(aliases.get(alias) != null) {
			newAlias = getNextAlias(aliases,alias+x,x+1);
		}
		return newAlias;
	}
	
	public static DetachedCriteria loopCriterias(DetachedCriteria criteria, List<Triplet<String,CriteriaExample,JoinType>> criterias) {
		if(criterias != null)
			for(Triplet<String,CriteriaExample,JoinType> c: criterias) {
				DetachedCriteria example = criteria.createCriteria(c.getValue0(),c.getValue0(),c.getValue2());
				for(Criterion criterion : c.getValue1().getCriterions())
					example.add(criterion);
				example.add(c.getValue1().getExample());
				
				example = loopCriterias(example, c.getValue1().getCriterias());
			}
		return criteria;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
	public static String getProjectPath() {
		boolean isRunningOnXampp = false;
		
		if(isRunningOnXampp)
			return "C:/Users/BJZE/Downloads/xampp/tomcat/webapps/Api/WEB-INF/classes/";
		else
			return Helper.class.getProtectionDomain().getCodeSource().getLocation().toString();
	}
	
	public static Date cloneDate(Date data) {
		if(data != null)
			return new Date(data.getTime());
		return data;
	}
	
	private static <E> void swap(List<E> a, int i, int j) {
        if (i != j) {
            E temp = a.get(i);
            a.set(i, a.get(j));
            a.set(j, temp);
        }
    }
	
	public static <E extends Comparable<E>> void simpleSort(List<E> a) {
		if (isNotNull(a)) {
	        for (int i = 0; i < a.size()  - 1; i++) {
	            int smallest = i;
	            for (int j = i + 1; j < a.size(); j++) {
	                if (a.get(j).compareTo(a.get(smallest))<=0) {
	                    smallest = j;
	                }
	            }
	
	            swap(a, i, smallest);
	        }
		}
    }
	
	public static boolean isNull(List<?> list) {
		return list == null;
	}
	
	public static boolean isNull(GenericFilter filter) {
		return filter == null;
	}
	
	public static boolean isNotNull(List<?> list) {
		return list != null;
	}
	
	public static boolean isNotNull(GenericFilter filter) {
		return filter != null;
	}
	
	public static boolean isStringIn(String str, String[] array) {
		if(array != null && str != null) {
			for(String s : array) {
				if(str.equals(s)) {
					return true;
				}
			}
		}
		return false;
	}
}
