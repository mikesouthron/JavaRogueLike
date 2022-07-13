package org.southy.rl.map;

public class FoVRestrictiveShadowCasting {

    static void computeQuadrant(GameMap map, int pov_x, int pov_y, int max_radius, boolean light_walls, int dx, int dy, double[] start_angle,
            double[] end_angle) {
        {
            int iteration = 1; /* iteration of the algo for this octant */
            boolean done = false;
            int total_obstacles = 0;
            int obstacles_in_last_line = 0;
            double min_angle = 0.0;
            int x;
            int y;

    /* do while there are unblocked slopes left and the algo is within the map's boundaries
       scan progressive lines/columns from the PC outwards */
            y = pov_y + dy; /* the outer slope's coordinates (first processed line) */
            if (y < 0 || y >= map.height) {
                done = true;
            }
            while (!done) {
                /* process cells in the line */
                double slopes_per_cell = 1.0 / (double) (iteration);
                double half_slopes = slopes_per_cell * 0.5;
                int processed_cell = (int) ((min_angle + half_slopes) / slopes_per_cell);
                int minx = Math.max(0, pov_x - iteration);
                int maxx = Math.min(map.width - 1, pov_x + iteration);
                done = true;
                for (x = pov_x + (processed_cell * dx); x >= minx && x <= maxx; x += dx) {
                    int c = x + (y * map.width);
                    /* calculate slopes per cell */
                    boolean visible = true;
                    boolean extended = false;
                    double centre_slope = (double) processed_cell * slopes_per_cell;
                    double start_slope = centre_slope - half_slopes;
                    double end_slope = centre_slope + half_slopes;
                    if (obstacles_in_last_line > 0) {
                        if (!(map.tiles[c - (map.width * dy)].fov && map.tiles[c - (map.width * dy)].transparent) &&
                                !(map.tiles[c - (map.width * dy) - dx].fov && map.tiles[c - (map.width * dy) - dx].transparent)) {
                            visible = false;
                        } else {
                            int idx;
                            for (idx = 0; idx < obstacles_in_last_line && visible; ++idx) {
                                if (start_slope <= end_angle[idx] && end_slope >= start_angle[idx]) {
                                    if (map.tiles[c].transparent) {
                                        if (centre_slope > start_angle[idx] && centre_slope < end_angle[idx]) {
                                            visible = false;
                                        }
                                    } else {
                                        if (start_slope >= start_angle[idx] && end_slope <= end_angle[idx]) {
                                            visible = false;
                                        } else {
                                            start_angle[idx] = Math.min(start_angle[idx], start_slope);
                                            end_angle[idx] = Math.max(end_angle[idx], end_slope);
                                            extended = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (visible) {
                        done = false;
                        map.tiles[c].fov = true;
                        /* if the cell is opaque, block the adjacent slopes */
                        if (!map.tiles[c].transparent) {
                            if (min_angle >= start_slope) {
                                min_angle = end_slope;
              /* if min_angle is applied to the last cell in line, nothing more
                 needs to be checked. */
                                if (processed_cell == iteration) {
                                    done = true;
                                }
                            } else if (!extended) {
                                start_angle[total_obstacles] = start_slope;
                                end_angle[total_obstacles++] = end_slope;
                            }
                            if (!light_walls) {
                                map.tiles[c].fov = false;
                            }
                        }
                    }
                    processed_cell++;
                }
                if (iteration == max_radius) {
                    done = true;
                }
                iteration++;
                obstacles_in_last_line = total_obstacles;
                y += dy;
                if (y < 0 || y >= map.height) {
                    done = true;
                }
            }
        }
        /* octant: horizontal edge */
        {
            int iteration = 1; /* iteration of the algo for this octant */
            boolean done = false;
            int total_obstacles = 0;
            int obstacles_in_last_line = 0;
            double min_angle = 0.0;
            int x;
            int y;

    /* do while there are unblocked slopes left and the algo is within the map's boundaries
       scan progressive lines/columns from the PC outwards */
            x = pov_x + dx; /*the outer slope's coordinates (first processed line) */
            if (x < 0 || x >= map.width) {
                done = true;
            }
            while (!done) {
                /* process cells in the line */
                double slopes_per_cell = 1.0 / (double) (iteration);
                double half_slopes = slopes_per_cell * 0.5;
                int processed_cell = (int) ((min_angle + half_slopes) / slopes_per_cell);
                int miny = Math.max(0, pov_y - iteration);
                int maxy = Math.min(map.height - 1, pov_y + iteration);
                done = true;
                for (y = pov_y + (processed_cell * dy); y >= miny && y <= maxy; y += dy) {
                    int c = x + (y * map.width);
                    /* calculate slopes per cell */
                    boolean visible = true;
                    boolean extended = false;
                    double centre_slope = (double) processed_cell * slopes_per_cell;
                    double start_slope = centre_slope - half_slopes;
                    double end_slope = centre_slope + half_slopes;
                    if (obstacles_in_last_line > 0) {
                        if (!(map.tiles[c - dx].fov && map.tiles[c - dx].transparent) &&
                                !(map.tiles[c - (map.width * dy) - dx].fov && map.tiles[c - (map.width * dy) - dx].transparent)) {
                            visible = false;
                        } else {
                            int idx;
                            for (idx = 0; idx < obstacles_in_last_line && visible; ++idx) {
                                if (start_slope <= end_angle[idx] && end_slope >= start_angle[idx]) {
                                    if (map.tiles[c].transparent) {
                                        if (centre_slope > start_angle[idx] && centre_slope < end_angle[idx]) {
                                            visible = false;
                                        }
                                    } else {
                                        if (start_slope >= start_angle[idx] && end_slope <= end_angle[idx]) {
                                            visible = false;
                                        } else {
                                            start_angle[idx] = Math.min(start_angle[idx], start_slope);
                                            end_angle[idx] = Math.max(end_angle[idx], end_slope);
                                            extended = true;
                                        }
                                    }
                                    ++idx;
                                }
                            }
                        }
                    }
                    if (visible) {
                        done = false;
                        map.tiles[c].fov = true;
                        /* if the cell is opaque, block the adjacent slopes */
                        if (!map.tiles[c].transparent) {
                            if (min_angle >= start_slope) {
                                min_angle = end_slope;
              /* if min_angle is applied to the last cell in line, nothing more
                 needs to be checked. */
                                if (processed_cell == iteration) {
                                    done = true;
                                }
                            } else if (!extended) {
                                start_angle[total_obstacles] = start_slope;
                                end_angle[total_obstacles++] = end_slope;
                            }
                            if (!light_walls) {
                                map.tiles[c].fov = false;
                            }
                        }
                    }
                    processed_cell++;
                }
                if (iteration == max_radius) {
                    done = true;
                }
                iteration++;
                obstacles_in_last_line = total_obstacles;
                x += dx;
                if (x < 0 || x >= map.width) {
                    done = true;
                }
            }
        }
    }

    static boolean compute(GameMap map, int povx, int povy, int maxRadius, boolean lightWalls) {
        /* set PC's position as visible */
        map.tiles[povx + (povy * map.width)].fov = true;

        /* calculate an approximated (excessive, just in case) maximum number of obstacles per octant */
        int maxObstacles = map.tiles.length / 7;

        double[] startAngle = new double[maxObstacles];
        double[] endAngle = new double[maxObstacles];

        /* compute the 4 quadrants of the map */
        computeQuadrant(map, povx, povy, maxRadius, lightWalls, 1, 1, startAngle, endAngle);
        computeQuadrant(map, povx, povy, maxRadius, lightWalls, 1, -1, startAngle, endAngle);
        computeQuadrant(map, povx, povy, maxRadius, lightWalls, -1, 1, startAngle, endAngle);
        computeQuadrant(map, povx, povy, maxRadius, lightWalls, -1, -1, startAngle, endAngle);

        for (int i = 0; i < map.tiles.length; i++) {
            if (map.tiles[i].fov) {
                map.visible[i] = map.tiles[i];
            }
        }

        return true;
    }

}
