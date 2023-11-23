import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static Map<Pattern, String> listOfPossibleMatches = new HashMap<>();
    static Path htmlFile;
    static String filePath;
    static String nameOfFile;
    static FileWriter texFile;
    static ArrayList<String> lines;
    static ArrayList<String> toBePrinted = new ArrayList<>();

    public static void main(String[] args) {
        listOfPossibleMatches.put(Pattern.compile("<meta[\\s\\S]*name=[\"']author[\"'][\\s\\S]*content=[\"']", Pattern.CASE_INSENSITIVE), "\\\\author{");
        listOfPossibleMatches.put(Pattern.compile("<h1[\\s\\S]*>[\\s\\S]*", Pattern.CASE_INSENSITIVE), "\\\\maketitle");
        listOfPossibleMatches.put(Pattern.compile("</h1>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<h2[^>]*>", Pattern.CASE_INSENSITIVE), "\\\\section{");
        listOfPossibleMatches.put(Pattern.compile("</h2>", Pattern.CASE_INSENSITIVE), "}\n");
        listOfPossibleMatches.put(Pattern.compile("<h3[^>]*>", Pattern.CASE_INSENSITIVE), "\\\\subsection{");
        listOfPossibleMatches.put(Pattern.compile("</h3>", Pattern.CASE_INSENSITIVE), "}\n");
        listOfPossibleMatches.put(Pattern.compile("<p[^>]*>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</p>", Pattern.CASE_INSENSITIVE), "\n");
        listOfPossibleMatches.put(Pattern.compile("<h2>", Pattern.CASE_INSENSITIVE), "\\\\section{");
        listOfPossibleMatches.put(Pattern.compile("<h3>", Pattern.CASE_INSENSITIVE), "\\\\subsection{");
        listOfPossibleMatches.put(Pattern.compile("<p>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<ul[^>]*>", Pattern.CASE_INSENSITIVE), "\\\\begin{itemize}\n");
        listOfPossibleMatches.put(Pattern.compile("<ul>", Pattern.CASE_INSENSITIVE), "\\\\begin{itemize}\n");
        listOfPossibleMatches.put(Pattern.compile("</ul>", Pattern.CASE_INSENSITIVE), "\\\\end{itemize}\n");
        listOfPossibleMatches.put(Pattern.compile("<ol[^>]*>", Pattern.CASE_INSENSITIVE), "\\\\begin{itemize}\n");
        listOfPossibleMatches.put(Pattern.compile("</ol>", Pattern.CASE_INSENSITIVE), "\\\\end{itemize}\n");
        listOfPossibleMatches.put(Pattern.compile("<li[^>]*>", Pattern.CASE_INSENSITIVE), "\\\\item ");
        listOfPossibleMatches.put(Pattern.compile("</li>", Pattern.CASE_INSENSITIVE), "\n");
        listOfPossibleMatches.put(Pattern.compile("<img.*src=[\"']", Pattern.CASE_INSENSITIVE), "\\\\begin{figure}\n\\\\centering\n\\\\includegraphics{");
        listOfPossibleMatches.put(Pattern.compile("<img.*", Pattern.CASE_INSENSITIVE), "\\\\begin{figure}\n\\\\centering\n");
        listOfPossibleMatches.put(Pattern.compile("src=[\"']", Pattern.CASE_INSENSITIVE), "\\\\includegraphics{");
        listOfPossibleMatches.put(Pattern.compile("<img[\\s\\S]*src=[\"']", Pattern.CASE_INSENSITIVE), "\\\\begin{figure}\n\\\\centering\n\\\\includegraphics{");
        listOfPossibleMatches.put(Pattern.compile("alt=.*>", Pattern.CASE_INSENSITIVE), "\\\\end{figure}\n");
        listOfPossibleMatches.put(Pattern.compile("<a.*href=[\"']", Pattern.CASE_INSENSITIVE), "\\\\href{");
        listOfPossibleMatches.put(Pattern.compile("</a>", Pattern.CASE_INSENSITIVE), "}\n");
        listOfPossibleMatches.put(Pattern.compile("<div[^>]*>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</div>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<span[^>]*>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</span>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<br>", Pattern.CASE_INSENSITIVE), "\n");
        listOfPossibleMatches.put(Pattern.compile("<hr>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<footer[^>]*>", Pattern.CASE_INSENSITIVE), "\\\\end{document}");
        listOfPossibleMatches.put(Pattern.compile("</footer>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<address[^>]*>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</address>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<!DOCTYPE html>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<html lang=[\"']en[\"']>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<head>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<meta charset=[\"']UTF-8[\"']>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<meta[\\s\\S]*name=[\"']viewport[\"'][\\s\\S]*>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<title>", Pattern.CASE_INSENSITIVE), "\\\\title{");
        listOfPossibleMatches.put(Pattern.compile("</title>", Pattern.CASE_INSENSITIVE), "}\n");
        listOfPossibleMatches.put(Pattern.compile("</head>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<body>", Pattern.CASE_INSENSITIVE), "\\\\begin{document}");
        listOfPossibleMatches.put(Pattern.compile("</body>", Pattern.CASE_INSENSITIVE), "\\\\end{document}");
        listOfPossibleMatches.put(Pattern.compile("<!--[\\s\\S]*-->", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<!--[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("[\\s\\S]*-->", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<script>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<script[\\s\\S]*>[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</script>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</html>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<section>", Pattern.CASE_INSENSITIVE), "\\\\section{");
        listOfPossibleMatches.put(Pattern.compile("</section>", Pattern.CASE_INSENSITIVE), "}\n");
        listOfPossibleMatches.put(Pattern.compile("<[iI]>", Pattern.CASE_INSENSITIVE), "\\\\textit{");
        listOfPossibleMatches.put(Pattern.compile("</[iI]>", Pattern.CASE_INSENSITIVE), "}");
        listOfPossibleMatches.put(Pattern.compile("<b>", Pattern.CASE_INSENSITIVE), "\\\\textbf{");
        listOfPossibleMatches.put(Pattern.compile("</b>", Pattern.CASE_INSENSITIVE), "}");
        listOfPossibleMatches.put(Pattern.compile("<details[\\s\\S]*>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</details>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<summary>", Pattern.CASE_INSENSITIVE), "\\\\section{");
        listOfPossibleMatches.put(Pattern.compile("</summary>", Pattern.CASE_INSENSITIVE), "}");
        listOfPossibleMatches.put(Pattern.compile("<blockquote cite=", Pattern.CASE_INSENSITIVE), "\\\\href{");
        listOfPossibleMatches.put(Pattern.compile("</blockquote>", Pattern.CASE_INSENSITIVE), "}");
        listOfPossibleMatches.put(Pattern.compile("<table.*>", Pattern.CASE_INSENSITIVE), "\\\\begin{centering}\n\\\\begin{tabular}{columns}\n\\\\hline");
        listOfPossibleMatches.put(Pattern.compile("</table>", Pattern.CASE_INSENSITIVE), "\\\\end{tabular}\n\\\\end{centering}\n");
        listOfPossibleMatches.put(Pattern.compile("</th>", Pattern.CASE_INSENSITIVE), " & ");
        listOfPossibleMatches.put(Pattern.compile("</tr>", Pattern.CASE_INSENSITIVE), "\\\\\\\\\n\\\\hline");
        listOfPossibleMatches.put(Pattern.compile("<th>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<tr>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("<td>", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("</td>", Pattern.CASE_INSENSITIVE), " & ");
        listOfPossibleMatches.put(Pattern.compile("<sup>", Pattern.CASE_INSENSITIVE), "\\\\textsuperscript{");
        listOfPossibleMatches.put(Pattern.compile("</sup>", Pattern.CASE_INSENSITIVE), "}");
        listOfPossibleMatches.put(Pattern.compile("[\\s\\S]*javascript[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("[\\s\\S]*Home[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("[\\s\\S]*About[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("[\\s\\S]*Team[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("[\\s\\S]*Link[\\s\\S]*", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("&#9776;", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("\\\\href\\{\\.\\./\\.\\./html/template/template\\.html\\}\\{\\d{4}\\}", Pattern.CASE_INSENSITIVE), "");
        listOfPossibleMatches.put(Pattern.compile("μ", Pattern.CASE_INSENSITIVE), "\\$\\\\mu\\$");
        listOfPossibleMatches.put(Pattern.compile("#", Pattern.CASE_INSENSITIVE), "\\\\#");
        listOfPossibleMatches.put(Pattern.compile("_", Pattern.CASE_INSENSITIVE), "\\\\_");
        listOfPossibleMatches.put(Pattern.compile("%", Pattern.CASE_INSENSITIVE), "\\\\%");
//        listOfPossibleMatches.put(Pattern.compile("\\\\", Pattern.CASE_INSENSITIVE), "\\\\\\\\");

        toBePrinted.add("\\documentclass{article}");
        toBePrinted.add("\\usepackage[margin=1.5cm]{geometry}");
        toBePrinted.add("\\usepackage{graphicx}");
        toBePrinted.add("\\usepackage{hyperref}");
        toBePrinted.add("\\usepackage{url}");
        toBePrinted.add(
                "\\newcommand{\\noimage}{%\n" +
                "  \\setlength{\\fboxsep}{-\\fboxrule}%\n" +
                "  \\fbox{\\phantom{\\rule{10pt}{10pt}}File missing\\phantom{\\rule{10pt}{10pt}}}% Framed box\n" +
                "}\n" +
                "\\let\\includegraphicsoriginal\\includegraphics\n" +
                "\\renewcommand{\\includegraphics}[2][width=\\textwidth]{\\IfFileExists{#2}{\\includegraphicsoriginal[#1]{#2}}{\\noimage}}");

        Scanner filePathIn = new Scanner(System.in);
        System.out.println("Please, insert relative file path: ");
        filePath = filePathIn.nextLine();
        nameOfFile = filePath.substring(0, filePath.lastIndexOf("."));


        try{
            htmlFile = Paths.get(filePath);
            lines = new ArrayList<>(Files.readAllLines(htmlFile).stream().toList());

            lines.stream().takeWhile(l -> !l.contains("<footer>")).forEach(data -> {
                String textag;
                String line = data;
                int columnCount = 0;
                if(!(line.isBlank() || line.isEmpty())){
                    for (Map.Entry<Pattern, String> entry : listOfPossibleMatches.entrySet()) {
                        Pattern k = entry.getKey();
                        textag = entry.getValue();
                        if (matchHtmlInFile(k, line)){
                            line = line.replaceAll(k.pattern(), textag);
                        }
                    }
                }

                if(line.contains("author{")){
                    line = line.replaceAll("\">", "}");
                }

                if(line.contains("href{")){
                    line = line.replaceAll("\">", "}{");
                }

                if(line.contains("href{") && line.contains("″>")){
                    line = line.replaceAll("″>", "}{");
                }

                if(line.contains("end{figure}") || line.contains("\\includegraphics{")){
                    line = line.replaceAll("[\"']", "}");
                }

                if(line.contains(".webp")){
                    line = line.replaceAll("\\.webp", ".jpg");
                }

                if(matchHtmlInFile(Pattern.compile("[a-zA-Z ]&[a-zA-Z ]"), line) && !(line.endsWith("&") || line.endsWith("& "))){
                    Matcher match = Pattern.compile("[a-zA-Z ]&[a-zA-Z ]").matcher(line);
                    String l = "";
                    if(match.find()){
                        l = match.group();
                        l = l.replaceAll("&", "\\\\\\\\&");
                    }
                    line = line.replaceAll("[a-zA-Z ]&[a-zA-Z ]", l);
                }

                if(line.contains("\\\\") && toBePrinted.get(toBePrinted.size() - 1).contains(" &")){
                    int n = 1;
                    while(toBePrinted.get(toBePrinted.size() - n).contains(" &") && !toBePrinted.get(toBePrinted.size() - n - 1).contains("\\begin{tabular}{columns}")){
                        n++;
                    }
                    if(toBePrinted.get(toBePrinted.size() - n - 1).contains("\\begin{tabular}{columns}")){
                        toBePrinted.set(toBePrinted.size() - n - 1, toBePrinted.get(toBePrinted.size() - n - 1).replaceAll("columns", "|p{5cm}".repeat(n) + "|"));
                    }
                    toBePrinted.set(toBePrinted.size() - 1, toBePrinted.get(toBePrinted.size() - 1).replaceAll(" &", ""));
                }

                if(line.contains(".html")){
                    StringBuffer b = new StringBuffer(line);
                    line = b.insert(b.indexOf("{") + 1, "run:").toString();
                }

                if(line.contains("C:") || line.contains("c:")){
                    StringBuffer b = new StringBuffer(line);
                    Pattern pattern = Pattern.compile("[a-zA-Z]:\\S+");
                    Matcher matcher = pattern.matcher(b);
                    if(line.contains("C:")) {
                        line = b.insert(b.indexOf("C:"), "\\path{")
                                .insert(matcher.find()? b.indexOf(matcher.group()) + matcher.group().length() : b.lastIndexOf(" ") - 2, "}")
                                .toString();
                    }
                    if(line.contains("c:")) {
                        line = b.insert(b.indexOf("c:"), "\\path{")
                                .insert(matcher.find()? b.indexOf(matcher.group()) + matcher.group().length() : b.lastIndexOf(" ") - 2, "}")
                                .toString();
                    }
                }


                if(line.contains(">") || line.contains("<")){
                    StringBuffer b = new StringBuffer(line);
                    if(line.contains("<")) line = b.insert(b.indexOf("<"), "$").insert(b.indexOf("<") + 1, "$").toString();
                    if(line.contains(">")) line = b.insert(b.indexOf(">"), "$").insert(b.indexOf(">") + 1, "$").toString();
                }

                if(line.contains("$") && !line.contains("\\mu") && !line.contains("<") && !line.contains(">")){
                    line = line.replace("$", "\\$");
                }

                if(!line.isBlank()){
                    toBePrinted.add(line.strip());
                }
            });

        } catch (IOException ex){
            System.out.println("Either the file doesn't exists or it has changed path/name.");
            ex.printStackTrace();
        }

        for(int i = 0; i <= toBePrinted.indexOf("\\begin{document}") + 1; i++){
            if(toBePrinted.get(i).matches("\\\\item")){
                toBePrinted.remove(toBePrinted.get(i));
            }
        }

        toBePrinted.remove(toBePrinted.indexOf("\\begin{document}") - 1);

        for(int i = toBePrinted.indexOf("\\maketitle") + 1; i < toBePrinted.indexOf("\\end{itemize}"); i++){
            toBePrinted.remove(i);
        }

        for(int i = toBePrinted.indexOf("\\maketitle") + 1; i < toBePrinted.indexOf("\\end{itemize}") + 1; i++){
            toBePrinted.remove(i);
        }

        toBePrinted.remove(toBePrinted.indexOf("\\end{itemize}"));

        toBePrinted.add("\\end{document}");
        try {
            texFile = new FileWriter(nameOfFile + ".tex");
            toBePrinted.stream().filter(l -> !l.matches("\\\\href\\{.*}\\{\\d{4}\\}")).forEach(l -> {
                try {
                    texFile.write(l + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("File .tex created at" + nameOfFile + ".tex");
            texFile.close();
        } catch (IOException e) {
            System.out.println("Something went wrong...");
            throw new RuntimeException(e);
        }

    }

    static boolean matchHtmlInFile(Pattern pattern, String line){
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    static ArrayList<String> saveElements(ArrayList<String> list, Pattern p, String line){
        return null;
    }

}