/**
 * Created by ChenLeiyuan on 01/11/2016.
 */
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
public class Project {
    public static void main(String[] args) {
        System.out.print("斗兽棋游戏");
        System.out.println();
        System.out.println();
        Scanner input1 = new Scanner(System.in);
        int animal[][]=new int [7][9]; //animal[][]数组用来存储动物当前状态
        int tile[][]=new int [7][9];//tile[][]数组用来存储地形当前状态
        stop://stop用来用户输入exit时跳出程序
        {
            while (true) {//此while（true）配合stop0实现重新开始游戏
                stop0://stop用来用户输入restart时重新开始游戏
                {
                    int currentStep = 0;//currentStep变量记录当前行动步数
                    int undoTimes=0;//undoTimes储存连续悔棋次数
                    int animalHistroy[][][] = new int[10000][7][9];//animalHistroy[][][]数组存储动物历史状态
                    begin(animal,tile);//begin方法用来对 animal，tile数组初始化
                    arrayCopy(animal,currentStep,animalHistroy);//arrayCopy方法用来将棋盘当前状态赋给棋盘历史状态的数组中
                    printHelp();// printHelp方法打印帮助
                    printMap(animal,tile);//printMap方法打印地图
                    while (true) {//此while true实现右方行动完回到左方继续行动
                        stop1://用于悔棋或者取消悔棋时跳出左方行动的while（true）
                        {
                            stop3://用于正常行动时跳出左方行动的while（true）
                            {
                                nextRow1://nextRow1配合下方while (true）实现当用户输入指令错误，或者用户输入指令为help时重新让用户输入
                                while (true) {
                                    System.out.print("左方玩家行动");
                                    String player1 = input1.nextLine();//提取用户输入
                                    System.out.print("\""+player1+"\"");//打印用户输入
                                    System.out.println();
                                    if (player1.equals("exit")) {
                                        break stop;
                                    } else if (player1.equals("restart")) {
                                        break stop0;
                                    } else if (player1.equals("help")) {
                                        printHelp();
                                        continue nextRow1;
                                    } else if (player1.equals("undo")) {
                                        if (currentStep > 0) {
                                            undoArrayCopy(animal, currentStep, animalHistroy);//将动物历史状态赋给动物当前状态
                                            currentStep--;//步数减1
                                            undoTimes++;//悔棋次数加1
                                            break stop1;
                                        } else {
                                            System.out.println("已经回到开局，不能再悔棋了");
                                            continue nextRow1;
                                        }
                                    } else if (player1.equals("redo")) {
                                        if (undoTimes > 0) {
                                            redoArrayCopy(animal, currentStep, animalHistroy);//将动物历史状态赋给动物当前状态
                                            currentStep++;
                                            undoTimes--;
                                            break stop1;
                                        } else {
                                            System.out.println("已经回到最后记录，不能再取消悔棋了");
                                            continue nextRow1;
                                        }
                                    }
                                    //判断用户输入的字符串长度是否为二
                                    if (player1.length() > 2 || player1.length() < 2) {
                                        System.out.println("不能识别指令"+"\""+player1+"\""+",请重新输入");
                                        continue nextRow1;
                                    }//judgeInput(player1)方法判断用户输入的字符串第一位是否为1-8的整数第二位是否为asdw中一个字母
                                    if (!judgeInput(player1)) {
                                        System.out.println("不能识别指令"+"\""+player1+"\""+"，请重新输入");
                                        continue nextRow1;
                                    }
                                    String animalplayer = "1" + player1.charAt(0);//因为我动物数组是用二位数存储，第一位1表示是左方动物，第一位为2表示右方动物
                                    int animalplayervalue = Integer.parseInt(animalplayer);
                                    int animalplayernumber = player1.charAt(0) - '0';
                                    int judge = 0;//judge用来判断动物是否已被消灭，如果未在当前动物数组中找到行动动物（即动物被消灭），judge值为0.如果找到，judge值非0，为当前行动动物的值
                                    for (int i = 0; i <= 6; i++) {//遍历数组，找到需行动动物的位置
                                        for (int j = 0; j <= 8; j++) {
                                            if (animal[i][j] == animalplayervalue) {
                                                judge = animalplayervalue;
                                               //将行动指令分a，s，d，w四种情况考虑
                                                switch (player1.charAt(1)) {
                                                    case 'a':
                                                        if (j > 0) {//在没有超过边界的情况下，先判断所要移动的目标位置地形信息是不是己方陷阱
                                                            if (tile[i][j - 1] == 2) {
                                                                if (animal[i][j - 1] / 10 != 1) {
                                                                    animal[i][j - 1] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop3;
                                                                }else {
                                                                    System.out.println("不能和友方单位重叠");
                                                                    continue nextRow1;
                                                                }
                                                            } else {//在移动的目标位置地形信息不是己方陷阱的情况下
                                                                int anotheranimalvalue = animal[i][j - 1];//anotheranimalvalue存储移动目标位置动物信息
                                                                switch (animalplayernumber) {
                                                                    //将移动动物分老鼠，老虎狮子，其他三类讨论
                                                                    case 1:
                                                                        if ((tile[i][j - 1] != 3) && ((anotheranimalvalue / 10) != 1)) {//在老鼠的情况下，先判断移动目标位置是不是己方的家或者有己方动物
                                                                            if (anotheranimalvalue == 0||anotheranimalvalue%10==1) {//移动目标位置不是己方的家且没有己方动物情况下，如果目标位置没有动物或者是老鼠，执行下列操作
                                                                                animal[i][j - 1] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop3;
                                                                            } else if (anotheranimalvalue % 10 == 8) {//如果移动目标位置动物为敌方大象
                                                                                if (tile[i][j] == 1) {//判断老鼠在不在水中
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow1;
                                                                                } else {
                                                                                    animal[i][j - 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 1]));//printName方法通过动物信息输出动物名字
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i][j - 1] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i][j - 1] != 3) && ((anotheranimalvalue / 10) != 1)) {//在动物信息为其他类的情况下，先判断移动目标位置是不是己方的家或者有己方动物
                                                                            if (tile[i][j - 1] != 1) {//再判断移动的目标位置地形是不是水
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {//在第三类中再分大象和其他两类，如果不是大象，直接通过移动动物信息第二位数字大小和目标位置动物第二位数字大小判断能否移动
                                                                                    animal[i][j - 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {//如果是大象，通过判断移动目标位置动物是不是老鼠判断大象能否移动
                                                                                    animal[i][j - 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 1]));
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i][j - 1] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能与友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i][j - 1] != 3) {//在动物信息为老虎狮子的情况下，先判断移动的目标位置是不是自己的家
                                                                            if (tile[i][j - 1] != 1) {//再判断移动的目标位置是不是水
                                                                                if (anotheranimalvalue / 10 != 1) {//在不是水的情况下
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j - 1] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 1]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {//在移动的目标位置是水的情况下
                                                                                if (animal[i][j - 1] != 21 && animal[i][j - 2] != 21 && animal[i][j - 3] != 21) {//判断有没有老鼠挡道
                                                                                    anotheranimalvalue = animal[i][j - 4];//在没有老鼠挡道的情况下，重置移动目标位置
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j - 4] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 4]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow1;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow1;
                                                        }
                                                    case 's':
                                                        if (i < 6) {
                                                            if (tile[i + 1][j] == 2) {
                                                                if (animal[i + 1][j] / 10 != 1) {
                                                                    animal[i + 1][j] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop3;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow1;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i + 1][j];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i + 1][j] != 3) && ((anotheranimalvalue / 10) != 1)) {
                                                                            if (anotheranimalvalue == 0 ||anotheranimalvalue%10==1) {
                                                                                animal[i + 1][j] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop3;
                                                                            } else if ((anotheranimalvalue % 10 == 8)) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow1;
                                                                                } else {
                                                                                    animal[i + 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i + 1][j] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i + 1][j] != 3) && ((anotheranimalvalue / 10) != 1)) {
                                                                            if (tile[i + 1][j] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i + 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i + 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i + 1][j] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i + 1][j] != 3) {
                                                                            if (tile[i + 1][j] != 1) {
                                                                                if (anotheranimalvalue / 10 != 1) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i + 1][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                if (animal[i + 1][j] != 21 && animal[i + 2][j] != 21) {
                                                                                    anotheranimalvalue = animal[i + 3][j];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i + 3][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 3][j]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow1;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow1;
                                                        }
                                                    case 'w':
                                                        if (i > 0) {
                                                            if (tile[i - 1][j] == 2) {
                                                                if (animal[i - 1][j] / 10 != 1) {
                                                                    animal[i - 1][j] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop3;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow1;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i - 1][j];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i - 1][j] != 3) && ((anotheranimalvalue / 10) != 1)) {
                                                                            if (anotheranimalvalue == 0||anotheranimalvalue%10==1) {
                                                                                animal[i - 1][j] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop3;
                                                                            } else if (anotheranimalvalue % 10 == 8) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow1;
                                                                                } else {
                                                                                    animal[i - 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i - 1][j]));
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i - 1][j] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i - 1][j] != 3) && ((anotheranimalvalue / 10) != 1)) {
                                                                            if (tile[i - 1][j] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i - 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i - 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i - 1][j]));
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i - 1][j] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i - 1][j] != 3) {
                                                                            if (tile[i - 1][j] != 1) {
                                                                                if (anotheranimalvalue / 10 != 1) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i - 1][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i - 1][j]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                if (animal[i - 1][j] != 21 && animal[i - 2][j] != 21) {
                                                                                    anotheranimalvalue = animal[i - 3][j];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i - 3][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i - 3][j]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow1;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow1;
                                                        }
                                                    case 'd':
                                                        if (j < 8) {
                                                            if (tile[i][j + 1] == 2) {
                                                                if (animal[i][j + 1] / 10 != 1) {
                                                                    animal[i][j + 1] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop3;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow1;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i][j + 1];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i][j + 1] != 3) && ((anotheranimalvalue / 10) != 1)) {
                                                                            if (anotheranimalvalue == 0||anotheranimalvalue%10==1) {
                                                                                animal[i][j + 1] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop3;
                                                                            } else if (anotheranimalvalue % 10 == 8) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow1;
                                                                                } else {
                                                                                    animal[i][j + 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 1]));
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i][j + 1] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i][j + 1] != 3) && ((anotheranimalvalue / 10) != 1)) {
                                                                            if (tile[i][j + 1] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i][j + 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i][j + 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop3;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 1]));
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow1;
                                                                            }
                                                                        } else if (tile[i][j + 1] == 3) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow1;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i][j + 1] != 3) {
                                                                            if (tile[i][j + 1] != 1) {
                                                                                if (anotheranimalvalue / 10 != 1) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j + 1] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 1]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow1;
                                                                                }
                                                                            } else {
                                                                                if (animal[i][j + 1] != 21 && animal[i][j + 2] != 21 && animal[i][j + 3] != 21) {
                                                                                    anotheranimalvalue = animal[i][j + 4];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j + 4] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop3;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 4]));
                                                                                        continue nextRow1;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow1;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow1;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow1;
                                                        }
                                                        break stop3;
                                                }
                                            }
                                        }
                                    }
                                    if (judge == 0) {
                                        System.out.println(printName(animalplayervalue) + "已经被消灭");
                                        continue nextRow1;
                                    }
                                }
                            }
                           //在正常行动后执行下列三行代码，重置连续悔棋次数，当前步数加一，将动物当前状态存到动物历史状态总
                            undoTimes = 0;
                            currentStep++;
                            arrayCopy(animal,currentStep,animalHistroy);
                        }
                        //在正常行动或者悔棋取消悔棋后都输出地图
                        printMap(animal, tile);
                        //判断左方玩家是否获胜
                        //judgeWin方法有返回值，当返回值为0时，左方玩家没有获胜，当返回值为1代表右方棋子被吃光，当返回值为2代表右方无子可动，当返回值为3代表我方棋子占领敌方的家

                        if (judgeWin(animal, tile, 2, 1, 5)!=0) {
                            System.out.println("左方玩家获胜");
                            if(judgeWin(animal, tile, 2, 1, 5)==1){
                                System.out.println("敌方棋子被吃光");
                            }else if(judgeWin(animal, tile, 2, 1, 5)==2){
                                System.out.println("敌方无子可动");
                            }else if(judgeWin(animal, tile, 2, 1, 5)==3){
                                System.out.println("我方棋子占领敌方的家");
                            }
                            System.out.println("输入restart重新开始");
                            //当一方获胜后可以通过输入restart重新开始游戏而不退出程序
                            String player1 = input1.nextLine();
                            if(player1.equals("restart"))
                                break stop0;
                            else
                            break stop;
                        }
//左方结束，右方行动
                        stop2:
                        {
                            stop4:
                            {


                                nextRow2:
                                while (true) {
                                    System.out.print("右方玩家行动");
                                    String player2 = input1.nextLine();
                                    System.out.print("\""+player2+"\"");
                                    System.out.println();
                                    if (player2.equals("exit")) {
                                        break stop;
                                    } else if (player2.equals("restart")) {
                                        break stop0;
                                    } else if (player2.equals("help")) {
                                        printHelp();
                                        continue nextRow2;
                                    } else if (player2.equals("undo")) {
                                        if (currentStep > 0) {
                                            undoArrayCopy(animal, currentStep, animalHistroy);
                                            currentStep--;
                                            undoTimes++;
                                            break stop2;
                                        } else {
                                            System.out.println("已经回到开局，不能再悔棋了");
                                            continue nextRow2;
                                        }
                                    } else if (player2.equals("redo")) {
                                        if (undoTimes > 0) {
                                            redoArrayCopy(animal, currentStep, animalHistroy);
                                            currentStep++;
                                            undoTimes--;
                                            break stop2;
                                        } else {
                                            System.out.println("已经回到最后记录，不能再取消悔棋了");
                                            continue nextRow2;
                                        }

                                    }
                                    if (player2.length()>  2 || player2.length() < 2) {
                                        System.out.println("不能识别指令\"+\"\\\"\"+player2+\"\\\"\"+\"，请重新输入");
                                        continue nextRow2;
                                    }
                                    if (!judgeInput(player2)) {
                                        System.out.println("不能识别指令\"+\"\\\"\"+player2+\"\\\"\"+\"，，请重新输入");
                                        continue nextRow2;
                                    }
                                    String animalplayer = "2" + player2.charAt(0);
                                    int animalplayervalue = Integer.parseInt(animalplayer);
                                    int animalplayernumber = player2.charAt(0) - '0';
                                    int judge = 0;
                                    for (int i = 0; i <= 6; i++) {
                                        for (int j = 0; j <= 8; j++) {
                                            if (animal[i][j] == animalplayervalue) {
                                                judge = animalplayervalue;
                                                switch (player2.charAt(1)) {
                                                    case 'a':
                                                        if (j > 0) {
                                                            if (tile[i][j - 1] == 4) {
                                                                if (animal[i][j - 1] / 10 != 2) {
                                                                    animal[i][j - 1] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop4;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow2;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i][j - 1];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i][j - 1] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (anotheranimalvalue == 0 ||anotheranimalvalue%10==1) {
                                                                                animal[i][j - 1] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop4;
                                                                            } else if (anotheranimalvalue % 10 == 8) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow2;
                                                                                } else {
                                                                                    animal[i][j - 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 1]));
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i][j - 1] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i][j - 1] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (tile[i][j - 1] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i][j - 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i][j - 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 1]));
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i][j - 1] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i][j - 1] != 5) {
                                                                            if (tile[i][j - 1] != 1) {
                                                                                if (anotheranimalvalue / 10 != 2) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j - 1] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 1]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                if (animal[i][j - 1] != 11 && animal[i][j - 2] != 11 && animal[i][j - 3] != 11) {
                                                                                    anotheranimalvalue = animal[i][j - 4];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j - 4] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j - 4]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow2;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow2;
                                                        }
                                                    case 's':
                                                        if (i < 6) {
                                                            if (tile[i + 1][j] == 4) {
                                                                if (animal[i + 1][j] / 10 != 2) {
                                                                    animal[i + 1][j] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop4;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow2;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i + 1][j];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i + 1][j] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (anotheranimalvalue == 0||anotheranimalvalue%10==1) {
                                                                                animal[i + 1][j] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop4;
                                                                            } else if (anotheranimalvalue % 10 == 8) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow2;
                                                                                } else {
                                                                                    animal[i + 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i + 1][j] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i + 1][j] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (tile[i + 1][j] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i + 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i + 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i + 1][j] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i + 1][j] != 5) {
                                                                            if (tile[i + 1][j] != 1) {
                                                                                if (anotheranimalvalue / 10 != 2) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i + 1][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                if (animal[i + 1][j] != 11 && animal[i + 2][j] != 11) {
                                                                                    anotheranimalvalue = animal[i + 3][j];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i + 3][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 3][j]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow2;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow2;
                                                        }
                                                    case 'w':
                                                        if (i > 0) {
                                                            if (tile[i - 1][j] == 4) {
                                                                if (animal[i - 1][j] / 10 != 2) {
                                                                    animal[i - 1][j] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop4;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow2;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i - 1][j];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i - 1][j] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (anotheranimalvalue == 0||anotheranimalvalue%10==1) {
                                                                                animal[i - 1][j] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop4;
                                                                            } else if (anotheranimalvalue % 10 == 8) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow2;
                                                                                } else {
                                                                                    animal[i - 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i - 1][j]) + "打不过" + printName(animal[i - 1][j]));
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i - 1][j] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i - 1][j] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (tile[i - 1][j] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i - 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i - 1][j] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i - 1][j] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i - 1][j] != 5) {
                                                                            if (tile[i - 1][j] != 1) {
                                                                                if (anotheranimalvalue / 10 != 2) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i - 1][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i + 1][j]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                if (animal[i - 1][j] != 11 && animal[i - 2][j] != 11) {
                                                                                    anotheranimalvalue = animal[i - 3][j];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i - 3][j] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i -3][j]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow2;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow2;
                                                        }
                                                    case 'd':
                                                        if (j < 8) {
                                                            if (tile[i][j + 1] == 4) {
                                                                if (animal[i][j + 1] / 10 != 2) {
                                                                    animal[i][j + 1] = animal[i][j];
                                                                    animal[i][j] = 0;
                                                                    break stop4;
                                                                }else{
                                                                    System.out.println("不能与友方单位重叠");
                                                                    continue nextRow2;
                                                                }
                                                            } else {
                                                                int anotheranimalvalue = animal[i][j + 1];
                                                                switch (animalplayernumber) {
                                                                    case 1:
                                                                        if ((tile[i][j + 1] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (anotheranimalvalue == 0||anotheranimalvalue%10==1) {
                                                                                animal[i][j + 1] = animal[i][j];
                                                                                animal[i][j] = 0;
                                                                                break stop4;
                                                                            } else if ((anotheranimalvalue % 10 == 8)) {
                                                                                if (tile[i][j] == 1) {
                                                                                    System.out.println("水中老鼠不能吃岸上的象");
                                                                                    continue nextRow2;
                                                                                } else {
                                                                                    animal[i][j + 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 1]));
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i][j + 1] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 2:
                                                                    case 3:
                                                                    case 4:
                                                                    case 5:
                                                                    case 8:
                                                                        if ((tile[i][j + 1] != 5) && ((anotheranimalvalue / 10) != 2)) {
                                                                            if (tile[i][j + 1] != 1) {
                                                                                if (animalplayernumber != 8 && ((anotheranimalvalue % 10) <= animalplayernumber)) {
                                                                                    animal[i][j + 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else if (animalplayernumber == 8 && (anotheranimalvalue % 10) != 1) {
                                                                                    animal[i][j + 1] = animal[i][j];
                                                                                    animal[i][j] = 0;
                                                                                    break stop4;
                                                                                } else {
                                                                                    System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 1]));
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                System.out.println(printName(animal[i][j]) + "不能下水");
                                                                                continue nextRow2;
                                                                            }
                                                                        } else if (tile[i][j + 1] == 5) {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        } else {
                                                                            System.out.println("不能和友方单位重叠");
                                                                            continue nextRow2;
                                                                        }
                                                                    case 6:
                                                                    case 7:
                                                                        if (tile[i][j + 1] != 5) {
                                                                            if (tile[i][j + 1] != 1) {
                                                                                if (anotheranimalvalue / 10 != 2) {
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j + 1] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 1]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("不能和友方单位重叠");
                                                                                    continue nextRow2;
                                                                                }
                                                                            } else {
                                                                                if (animal[i][j + 1] != 11 && animal[i][j + 2] != 11 && animal[i][j + 3] != 11) {
                                                                                    anotheranimalvalue = animal[i][j + 4];
                                                                                    if ((anotheranimalvalue % 10) <= animalplayernumber) {
                                                                                        animal[i][j + 4] = animal[i][j];
                                                                                        animal[i][j] = 0;
                                                                                        break stop4;
                                                                                    } else {
                                                                                        System.out.println(printName(animal[i][j]) + "打不过" + printName(animal[i][j + 4]));
                                                                                        continue nextRow2;
                                                                                    }
                                                                                } else {
                                                                                    System.out.println("老鼠挡道");
                                                                                    continue nextRow2;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            System.out.println("不能走进自己的家");
                                                                            continue nextRow2;
                                                                        }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("不能走出边界");
                                                            continue nextRow2;
                                                        }
                                                }
                                            }
                                        }
                                    }
                                    if (judge == 0) {
                                        System.out.println(printName(animalplayervalue) + "已经被消灭");
                                        continue nextRow2;
                                    }
                                }

                            }
                            undoTimes = 0;
                            currentStep++;
                            arrayCopy(animal,currentStep,animalHistroy);
                        }
                        printMap(animal, tile);
                        if (judgeWin(animal, tile,1, 2, 3)!=0) {
                            System.out.println("右方玩家获胜");
                            if(judgeWin(animal, tile,1, 2, 3)==1){
                                System.out.println("敌方棋子被吃光");
                            }else if(judgeWin(animal, tile,1, 2, 3)==2){
                                System.out.println("敌方无子可动");
                            }else if(judgeWin(animal, tile,1, 2, 3)==3){
                                System.out.println("我方棋子占领对方的家");
                            }
                            System.out.println("输入restart重新开始");
                            String player2 = input1.nextLine();
                            if(player2.equals("restart"))
                                break stop0;
                            else
                                break stop;
                        }
                    }
                }        //右方结束
            }
        }
    }
    //方法一：printMap方法打印地图  根据当前动物信息，地形信息输出地图
    public static void printMap(int animal[][], int tile[][]) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                int tile0 = tile[i][j];
                int animal0 =  animal[i][j];
                if (animal0 == 0 && tile0 == 0)
                    System.out.print("    ");
                else if (animal0 == 0 && tile0 == 1)
                    System.out.print(" 水 ");
                else if (animal0 == 0 && tile0 == 2)
                    System.out.print(" 陷 ");
                else if (animal0 == 0 & tile0 == 3)
                    System.out.print(" 家 ");
                else if (animal0 == 0 && tile0 == 4)
                    System.out.print(" 陷 ");
                else if (animal0 == 0 && tile0 == 5)
                    System.out.print(" 家 ");
                else if (animal0 == 11)
                    System.out.print("1鼠 ");
                else if (animal0 == 12)
                    System.out.print("2猫 ");
                else if (animal0 == 13)
                    System.out.print("3狼 ");
                else if (animal0 == 14)
                    System.out.print("4狗 ");
                else if (animal0 == 15)
                    System.out.print("5豹 ");
                else if (animal0 == 16)
                    System.out.print("6虎 ");
                else if (animal0 == 17)
                    System.out.print("7狮 ");
                else if (animal0 == 18)
                    System.out.print("8象 ");
                else if (animal0 == 21)
                    System.out.print(" 鼠1");
                else if (animal0 == 22)
                    System.out.print(" 猫2");
                else if (animal0 == 23)
                    System.out.print(" 狼3");
                else if (animal0 == 24)
                    System.out.print(" 狗4");
                else if (animal0 == 25)
                    System.out.print(" 豹5");
                else if (animal0 == 26)
                    System.out.print(" 虎6");
                else if (animal0 == 27)
                    System.out.print(" 狮7");
                else if (animal0 == 28)
                    System.out.print(" 象8");
            }
            System.out.println();
        }
        System.out.println();
    }
    //方法二 printHelp方法打印帮助
    public static void printHelp() {
        System.out.println("指令介绍：");
        System.out.println("");
        System.out.println("1.移动指令");
        System.out.println("\t移动指令由两部分组成。");
        System.out.println("\t第一部分是数字1-8，根据战斗力分别对应鼠（1），猫（2），狼（3），狗（4），豹（5），虎（6），狮（7），象（8）");
        System.out.println("\t第二部分是字母wasd中的一个，w对应上方向，a对应左方向，s对应下方向，d对应右方向");
        System.out.println("");
        System.out.println("2.游戏指令");
        System.out.println("\t输入 restart 重新开始游戏");
        System.out.println("\t输入 help 查看帮助");
        System.out.println("\t输入 undo 悔棋");
        System.out.println("\t输入 redo 取消悔棋");
        System.out.println("\t输入 exit 退出游戏");
        System.out.println("");

    }
    //方法三 printName方法打印动物名字 根据动物信息，输出动物名字
    public static String printName(int animal) {
        String result;
        if (animal % 10 == 1) {
            result = "鼠";
        } else if (animal % 10 == 2) {
            result = "猫";
        } else if (animal % 10 == 3) {
            result = "狼";
        } else if (animal % 10 == 4) {
            result = "狗";
        } else if (animal % 10 == 5) {
            result = "豹";
        } else if (animal % 10 == 6) {
            result = "虎";
        } else if (animal % 10 == 7) {
            result = "狮";
        } else {
            result = "象";
        }
        return result;
    }
    //方法四 arrayCopy方法数组复制(用于行动) 将当前动物状态数组复制到保存动物历史状态的数组中
    public static void arrayCopy(int[][]animal, int currentStep, int animalHistroy[][][]) {
        for(int k=currentStep;k<10000;k++){
            for(int i=0;i<7;i++){
                for(int j=0;j<9;j++){
                    animalHistroy[k][i][j]=animal[i][j];
                }
            }
        }
    }
    //方法五 undoArrayCopy方法数组复制(用于悔棋) 提取动物历史状态的数组存到当前动物状态的数组中
    public static void undoArrayCopy(int[][]animal, int currentStep, int animalHistroy[][][]) {
        for(int i=0;i<7;i++){
            for(int j=0;j<9;j++){
                animal[i][j]= animalHistroy[currentStep-1][i][j];
            }
        }
    }
    //方法六 redoArrayCopy方法数组复制(用于取消悔棋)  提取动物历史状态的数组存到当前动物状态的数组中
    public static void redoArrayCopy(int[][]animal, int currentStep, int animalHistroy[][][]) {
        for(int i=0;i<7;i++){
            for(int j=0;j<9;j++){
                animal[i][j]= animalHistroy[currentStep+1][i][j];
            }
        }
    }
    //方法七 胜负判断一：判断一方棋是否被吃光 即遍历当前动物状态数组看看是否有左方或者右方动物
    public static Boolean judge1(int[][] animal, int number) {
        Boolean result = true;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animal[i][j]/10 == number) {
                    result = false;
                }
            }
        }
        return result;
    }
    //方法八 胜负判断二：判断一方是否无子可动
    public static Boolean judge2(int animal[][], int tile[][], int number1, int number2, int number3) {
        //number1 代表被判断方动物信息十位数字,number2 代表另一方动物信息十位数字,number3 代表移动方地形信息中家的代表数字
        Boolean result = true;
        stop:
        {
            for (int i = 0; i <= 6; i++) {
                for (int j = 0; j <= 8; j++) {//遍历数组，找到当前还活着的棋子
                    if (animal[i][j] / 10 == number1) {
                        //将动物所处位置分成九种分别判断
                        if (i > 0 && i < 6 && j > 0 && j < 8) {
                            //判断前后左右是否存在陷阱，如果存在陷阱而且己方动物不在陷阱里，代表动物可以行动
                            if (tile[i][j - 1] == number3 - 1 && animal[i][j - 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i][j + 1] == number3 - 1 && animal[i][j + 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i - 1][j] == number3 - 1 && animal[i - 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i + 1][j] == number3 - 1 && animal[i + 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    //将动物分为鼠，虎狮，象，猫狗狼豹四种分别判断其是否可移动
                                    case 1:
                                        if (tile[i][j - 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j - 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j - 1] == 0 || animal[i][j - 1] == number2 * 10 + 8 || animal[i][j - 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j + 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j + 1] == 0 || animal[i][j + 1] == number2 * 10 + 8 || animal[i][j + 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i - 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i - 1][j] == 0 || animal[i - 1][j] == number2 * 10 + 8 || animal[i - 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i + 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i + 1][j] == 0 || animal[i + 1][j] == number2 * 10 + 8 || animal[i + 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1 ) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1 ) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1)  {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1 ) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j - 1] != number3 &&  tile[i][j-1] != 1 ) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j - 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j + 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i + 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i - 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j - 1] == 1) {
                                            if (animal[i][j - 1] != number2 * 10 + 1 && animal[i][j - 2] != number2 * 10 + 1 && animal[i][j - 3] != number2 * 10 + 1) {
                                                if (animal[i][j - 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }

                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j + 1] == 1) {
                                            if (animal[i][j + 1] != number2 * 10 + 1 && animal[i][j + 2] != number2 * 10 + 1 && animal[i][j + 3] != number2 * 10 + 1) {
                                                if (animal[i][j + 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i + 1][j] == 1) {
                                            if (animal[i + 1][j] != number2 * 10 + 1 && animal[i + 2][j] != number2 * 10 + 1) {
                                                if (animal[i + 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i - 1][j] == 1) {
                                            if (animal[i - 1][j] != number2 * 10 + 1 && animal[i - 2][j] != number2 * 10 + 1) {
                                                if (animal[i - 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }

                        } else if (i == 0 && j == 0) {
                            if (tile[i][j + 1] == number3 - 1 && animal[i][j + 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i + 1][j] == number3 - 1 && animal[i + 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    case 1:
                                        if (tile[i][j + 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j + 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j + 1] == 0 || animal[i][j + 1] == number2 * 10 + 8 || animal[i][j + 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i + 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i + 1][j] == 0 || animal[i + 1][j] == number2 * 10 + 8 || animal[i + 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j + 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i + 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j + 1] == 1) {
                                            if (animal[i][j + 1] != number2 * 10 + 1 && animal[i][j + 2] != number2 * 10 + 1 && animal[i][j + 3] != number2 * 10 + 1) {
                                                if (animal[i][j + 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i + 1][j] == 1) {
                                            if (animal[i + 1][j] != number2 * 10 + 1 && animal[i + 2][j] != number2 * 10 + 1) {
                                                if (animal[i + 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (i == 0 && j == 8) {
                            //判断前后左右是否存在陷阱
                            if (tile[i][j - 1] == number3 - 1 && animal[i][j - 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i + 1][j] == number3 - 1 && animal[i + 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    //将动物分为鼠，虎狮，象，猫狗狼豹四种分别判断其是否可移动
                                    case 1:
                                        if (tile[i][j - 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j - 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j - 1] == 0 || animal[i][j - 1] == number2 * 10 + 8 || animal[i][j - 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i + 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i + 1][j] == 0 || animal[i + 1][j] == number2 * 10 + 8 || animal[i + 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j - 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i + 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j - 1] == 1) {
                                            if (animal[i][j - 1] != number2 * 10 + 1 && animal[i][j - 2] != number2 * 10 + 1 && animal[i][j - 3] != number2 * 10 + 1) {
                                                if (animal[i][j - 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i + 1][j] == 1) {
                                            if (animal[i + 1][j] != number2 * 10 + 1 && animal[i + 2][j] != number2 * 10 + 1) {
                                                if (animal[i + 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (i == 6 && j == 0) {
                            //判断前后左右是否存在陷阱
                            if (tile[i][j + 1] == number3 - 1 && animal[i][j + 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i - 1][j] == number3 - 1 && animal[i - 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    //将动物分为鼠，虎狮，象，猫狗狼豹四种分别判断其是否可移动
                                    case 1:
                                        if (tile[i][j + 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j + 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j + 1] == 0 || animal[i][j + 1] == number2 * 10 + 8 || animal[i][j + 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i - 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i - 1][j] == 0 || animal[i - 1][j] == number2 * 10 + 8 || animal[i - 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j + 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i - 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j + 1] == 1) {
                                            if (animal[i][j + 1] != number2 * 10 + 1 && animal[i][j + 2] != number2 * 10 + 1 && animal[i][j + 3] != number2 * 10 + 1) {
                                                if (animal[i][j + 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i - 1][j] == 1) {
                                            if (animal[i - 1][j] != number2 * 10 + 1 && animal[i - 2][j] != number2 * 10 + 1) {
                                                if (animal[i - 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (i == 6 && j == 8) {
                            //判断前后左右是否存在陷阱
                            if (tile[i][j - 1] == number3 - 1 && animal[i][j - 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i - 1][j] == number3 - 1 && animal[i - 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    //将动物分为鼠，虎狮，象，猫狗狼豹四种分别判断其是否可移动
                                    case 1:
                                        if (tile[i][j - 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j - 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j - 1] == 0 || animal[i][j - 1] == number2 * 10 + 8 || animal[i][j - 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i - 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i - 1][j] == 0 || animal[i - 1][j] == number2 * 10 + 8 || animal[i - 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j - 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i - 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j - 1] == 1) {
                                            if (animal[i][j - 1] != number2 * 10 + 1 && animal[i][j - 2] != number2 * 10 + 1 && animal[i][j - 3] != number2 * 10 + 1) {
                                                if (animal[i][j - 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i - 1][j] == 1) {
                                            if (animal[i - 1][j] != number2 * 10 + 1 && animal[i - 2][j] != number2 * 10 + 1) {
                                                if (animal[i - 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (i == 0 && j > 0 && j < 8) {
                            //判断前后左右是否存在陷阱
                            if (tile[i][j - 1] == number3 - 1 && animal[i][j - 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i][j + 1] == number3 - 1 && animal[i][j + 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i + 1][j] == number3 - 1 && animal[i + 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    //将动物分为鼠，虎狮，象，猫狗狼豹四种分别判断其是否可移动
                                    case 1:
                                        if (tile[i][j - 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j - 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j - 1] == 0 || animal[i][j - 1] == number2 * 10 + 8 || animal[i][j - 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j + 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j + 1] == 0 || animal[i][j + 1] == number2 * 10 + 8 || animal[i][j + 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i + 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i + 1][j] == 0 || animal[i + 1][j] == number2 * 10 + 8 || animal[i + 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j - 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j + 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i + 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j - 1] == 1) {
                                            if (animal[i][j - 1] != number2 * 10 + 1 && animal[i][j - 2] != number2 * 10 + 1 && animal[i][j - 3] != number2 * 10 + 1) {
                                                if (animal[i][j - 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }

                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j + 1] == 1) {
                                            if (animal[i][j + 1] != number2 * 10 + 1 && animal[i][j + 2] != number2 * 10 + 1 && animal[i][j + 3] != number2 * 10 + 1) {
                                                if (animal[i][j + 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i + 1][j] == 1) {
                                            if (animal[i + 1][j] != number2 * 10 + 1 && animal[i + 2][j] != number2 * 10 + 1) {
                                                if (animal[i + 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (i == 6 && j > 0 && j < 8) {
                            if (tile[i][j - 1] == number3 - 1 && animal[i][j - 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i][j + 1] == number3 - 1 && animal[i][j + 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i - 1][j] == number3 - 1 && animal[i - 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    case 1:
                                        if (tile[i][j - 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j - 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j - 1] == 0 || animal[i][j - 1] == number2 * 10 + 8 || animal[i][j - 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j + 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j + 1] == 0 || animal[i][j + 1] == number2 * 10 + 8 || animal[i][j + 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i - 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i - 1][j] == 0 || animal[i - 1][j] == number2 * 10 + 8 || animal[i - 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j - 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j + 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i - 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j - 1] == 1) {
                                            if (animal[i][j - 1] != number2 * 10 + 1 && animal[i][j - 2] != number2 * 10 + 1 && animal[i][j - 3] != number2 * 10 + 1) {
                                                if (animal[i][j - 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }

                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j + 1] == 1) {
                                            if (animal[i][j + 1] != number2 * 10 + 1 && animal[i][j + 2] != number2 * 10 + 1 && animal[i][j + 3] != number2 * 10 + 1) {
                                                if (animal[i][j + 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i - 1][j] == 1) {
                                            if (animal[i - 1][j] != number2 * 10 + 1 && animal[i - 2][j] != number2 * 10 + 1) {
                                                if (animal[i - 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (j == 0 && i > 0 && i < 6) {
                            if (tile[i][j + 1] == number3 - 1 && animal[i][j + 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i - 1][j] == number3 - 1 && animal[i - 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i + 1][j] == number3 - 1 && animal[i + 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    case 1:
                                        if (tile[i][j + 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j + 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j + 1] == 0 || animal[i][j + 1] == number2 * 10 + 8 || animal[i][j + 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i - 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i - 1][j] == 0 || animal[i - 1][j] == number2 * 10 + 8 || animal[i - 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i + 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i + 1][j] == 0 || animal[i + 1][j] == number2 * 10 + 8 || animal[i + 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j + 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i + 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i - 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j + 1] != number3 && tile[i][j + 1] != 1) {
                                            if (animal[i][j + 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j + 1] == 1) {
                                            if (animal[i][j + 1] != number2 * 10 + 1 && animal[i][j + 2] != number2 * 10 + 1 && animal[i][j + 3] != number2 * 10 + 1) {
                                                if (animal[i][j + 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j + 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i + 1][j] == 1) {
                                            if (animal[i + 1][j] != number2 * 10 + 1 && animal[i + 2][j] != number2 * 10 + 1) {
                                                if (animal[i + 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i - 1][j] == 1) {
                                            if (animal[i - 1][j] != number2 * 10 + 1 && animal[i - 2][j] != number2 * 10 + 1) {
                                                if (animal[i - 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        } else if (j == 8 && i > 0 && i < 6) {
                            //判断前后左右是否存在陷阱
                            if (tile[i][j - 1] == number3 - 1 && animal[i][j - 1] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i - 1][j] == number3 - 1 && animal[i - 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else if (tile[i + 1][j] == number3 - 1 && animal[i + 1][j] / 10 != number1) {
                                result = false;
                                break stop;
                            } else {
                                switch (animal[i][j] % 10) {
                                    //将动物分为鼠，虎狮，象，猫狗狼豹四种分别判断其是否可移动
                                    case 1:
                                        if (tile[i][j - 1] != number3) {
                                            if (tile[i][j] == 1 && animal[i][j - 1] == number2 * 10 + 8) {
                                            } else if (animal[i][j - 1] == 0 || animal[i][j - 1] == number2 * 10 + 8 || animal[i][j - 1] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i - 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i - 1][j] == 0 || animal[i - 1][j] == number2 * 10 + 8 || animal[i - 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3) {
                                            if (tile[i][j] == 1 && animal[i + 1][j] == number2 * 10 + 8) {
                                            } else if (animal[i + 1][j] == 0 || animal[i + 1][j] == number2 * 10 + 8 || animal[i + 1][j] == number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j - 1] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i + 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i - 1][j] != number2 * 10 + 1) {
                                                result = false;
                                                break stop;
                                            }
                                        }
                                        break;
                                    case 6:
                                    case 7:
                                        if (tile[i][j - 1] != number3 && tile[i][j - 1] != 1) {
                                            if (animal[i][j - 1] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 1] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i][j - 1] == 1) {
                                            if (animal[i][j - 1] != number2 * 10 + 1 && animal[i][j - 2] != number2 * 10 + 1 && animal[i][j - 3] != number2 * 10 + 1) {
                                                if (animal[i][j - 4] / 10 != number1 && animal[i][j] % 10 >= animal[i][j - 4] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i + 1][j] != number3 && tile[i + 1][j] != 1) {
                                            if (animal[i + 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i + 1][j] == 1) {
                                            if (animal[i + 1][j] != number2 * 10 + 1 && animal[i + 2][j] != number2 * 10 + 1) {
                                                if (animal[i + 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i + 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        if (tile[i - 1][j] != number3 && tile[i - 1][j] != 1) {
                                            if (animal[i - 1][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 1][j] % 10) {
                                                result = false;
                                                break stop;
                                            }
                                        } else if (tile[i - 1][j] == 1) {
                                            if (animal[i - 1][j] != number2 * 10 + 1 && animal[i - 2][j] != number2 * 10 + 1) {
                                                if (animal[i - 3][j] / 10 != number1 && animal[i][j] % 10 >= animal[i - 3][j] % 10) {
                                                    result = false;
                                                    break stop;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        }

                    }
                }
            }
        }
        return result;

    }
    //方法九 胜负判断三：判断是否有一方棋子进入对方兽穴
    public static Boolean judge3(int[][]animal) {
        Boolean result = false;
        if (animal[3][0] != 0 || animal[3][8] != 0) {
            result = true;
        }
        return result;
    }
    //方法十 综合胜负判断一，二，三：判断玩家是否获胜
    public static int judgeWin(int animal[][], int tile[][], int number1, int number2, int number3) {
        //number1 代表需判断方动物信息十位数字,number2 代表另一方动物信息十位数字,number3 代表需判断方地形信息中家的代表数字
        int result = 0;

        if (judge3(animal)){
            result = 3;
        }else if(judge1(animal,number1)){
            result = 1;
        }else if(judge2(animal, tile,number1, number2 ,number3)){
            result = 2;
        }
        return result;
    }
    //方法十一 判断输入是否合法 第一位是否是1-8中一个数字，第二位是否是awsd中一个字母
    public static Boolean judgeInput(String input) {
        Boolean result = false;
        Boolean judge0 = false;
        Boolean judge1 = false;
        if (input.charAt(0) == '1' || input.charAt(0) == '2' || input.charAt(0) == '3' || input.charAt(0) == '4' || input.charAt(0) == '5' || input.charAt(0) == '6' || input.charAt(0) == '7' || input.charAt(0) == '8') {
            judge0 = true;
        }
        if (input.charAt(1) == 'w' || input.charAt(1) == 'a' || input.charAt(1) == 's' || input.charAt(1) == 'd') {
            judge1 = true;
        }
        if (judge0 && judge1) {
            result = true;
        }
        return result;
    }
    //方法十二 读取文件，animal，tile数组初始化
    public static void begin(int animal[][],int tile[][]){
 //从文件中读取animal信息
        File file = new File("animal.txt");
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//将信息赋给数组
        for(int i=0;i<7;i++){
            String animalString = input.next();
            for(int j=0;j<9;j++){
                animal[i][j]=Integer.parseInt(animalString.substring(2*j ,2*j+2));
            }
        }
//从文件中读取tile 信息
        File file2 = new File("tile.txt");
        Scanner input2 = null;
        try {
            input2 = new Scanner(file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//将信息赋给数组
        for(int i=0;i<7;i++){
            String tileString = input2.next();
            for(int j=0;j<9;j++){
                tile[i][j]=Integer.parseInt(tileString.substring(j ,j+1));
            }
        }

    }
}
